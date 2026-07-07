package org.mage.test.performance;

import mage.constants.PhaseStep;
import mage.constants.Zone;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mage.test.serverside.base.CardTestPlayerBase;

/**
 * Combat-scaling probe for the "N attackers vs N blockers" hot-path hypothesis.
 *
 * PURPOSE (feasibility experiment, NOT a finished ADO task):
 *   Measure how engine combat cost grows as N increases, and find whether/where
 *   a large all-out-attack + all-block combat blows up (super-linear time or OOM).
 *   Run with increasing N and read the "[SCALING] n=.. combat_ms=.." lines; plot
 *   ms vs N to see the complexity class (linear vs quadratic+).
 *
 * VERIFY FIRST (this file was authored but NOT compiled/run — the build env
 * could not reach Maven Central):
 *   The "declare one attacker/blocker per name per call, looped N times" idiom
 *   below assumes TestPlayer consumes a distinct same-named permanent on each
 *   attack()/block() call. If the harness instead errors or only acts on the
 *   first match, fix that idiom before trusting any numbers. Start at small N.
 *
 * Run examples (JDK 17, from repo root):
 *   mvn -pl Mage.Tests test -Dtest=CombatScalingTest#scale_0100
 *   mvn -pl Mage.Tests test -Dtest=CombatScalingTest -DargLine="-Xmx6g"
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CombatScalingTest extends CardTestPlayerBase {

    private void runScenario(int n) {
        // Attackers for the active player (playerA on turn 1): vanilla 2/2s.
        addCard(Zone.BATTLEFIELD, playerA, "Grizzly Bears", n);
        // Blockers for the defender: a *different-named* vanilla 2/2 so the
        // attacker/blocker names don't collide when declaring.
        addCard(Zone.BATTLEFIELD, playerB, "Silvercoat Lion", n);

        // Declaration idiom (matches XMage TestPlayer semantics):
        //  - Attackers: repeat attack(...,"Grizzly Bears"). selectAttackers' filter
        //    EXCLUDES already-declared attackers, so a fixed ":index" would shrink and
        //    skip creatures; repeating the bare name makes each call consume the NEXT
        //    available attacker -> N calls declare N distinct attackers.
        //  - Blockers: selectBlockers does NOT exclude already-blocking creatures, so
        //    address a DISTINCT blocker per call via "Name:index"; the attacker is
        //    referenced by its stable index among attacking creatures.
        for (int i = 0; i < n; i++) {
            attack(1, playerA, "Grizzly Bears");
        }
        for (int i = 0; i < n; i++) {
            block(1, playerB, "Silvercoat Lion:" + i, "Grizzly Bears:" + i);
        }

        setStopAt(1, PhaseStep.END_COMBAT);

        long t0 = System.nanoTime();
        execute();
        long ms = (System.nanoTime() - t0) / 1_000_000L;
        System.out.println("[SCALING] n=" + n + " combat_ms=" + ms);
    }

    @Test public void scale_0002() { runScenario(2); }
    @Test public void scale_0003() { runScenario(3); }
    @Test public void scale_0010() { runScenario(10); }
    @Test public void scale_0050() { runScenario(50); }
    @Test public void scale_0100() { runScenario(100); }
    @Test public void scale_0250() { runScenario(250); }
    @Test public void scale_0500() { runScenario(500); }
    @Test public void scale_1000() { runScenario(1000); }
    @Test public void scale_2000() { runScenario(2000); }
    @Test public void scale_4000() { runScenario(4000); }
    @Test public void scale_8000() { runScenario(8000); }
}
