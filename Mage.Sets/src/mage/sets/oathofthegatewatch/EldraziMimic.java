/*
 *  Copyright 2010 BetaSteward_at_googlemail.com. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY BetaSteward_at_googlemail.com ``AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL BetaSteward_at_googlemail.com OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and should not be interpreted as representing official policies, either expressed
 *  or implied, of BetaSteward_at_googlemail.com.
 */
package mage.sets.oathofthegatewatch;

import java.util.UUID;
import mage.MageInt;
import mage.abilities.Ability;
import mage.abilities.common.EntersBattlefieldControlledTriggeredAbility;
import mage.abilities.effects.ContinuousEffect;
import mage.abilities.effects.OneShotEffect;
import mage.abilities.effects.common.continuous.SetPowerToughnessSourceEffect;
import mage.cards.CardImpl;
import mage.constants.CardType;
import mage.constants.Duration;
import mage.constants.Outcome;
import mage.constants.Rarity;
import mage.constants.SetTargetPointer;
import mage.constants.SubLayer;
import mage.constants.Zone;
import mage.filter.common.FilterCreaturePermanent;
import mage.filter.predicate.mageobject.ColorlessPredicate;
import mage.filter.predicate.permanent.AnotherPredicate;
import mage.game.Game;
import mage.game.permanent.Permanent;
import mage.players.Player;

/**
 *
 * @author fireshoes
 */
public class EldraziMimic extends CardImpl {
    
    private static final FilterCreaturePermanent filter = new FilterCreaturePermanent("another colorless creature");
    
    static {
        filter.add(new AnotherPredicate());
        filter.add(new ColorlessPredicate());
    }

    public EldraziMimic(UUID ownerId) {
        super(ownerId, 2, "Eldrazi Mimic", Rarity.RARE, new CardType[]{CardType.CREATURE}, "{2}");
        this.expansionSetCode = "OGW";
        this.subtype.add("Eldrazi");
        this.power = new MageInt(2);
        this.toughness = new MageInt(1);

        // Whenever another colorless creature enters the battlefield under your control, you may have the base power and toughness of Eldrazi Mimic 
        // become that creature's power and toughness until end of turn.
        this.addAbility(new EntersBattlefieldControlledTriggeredAbility(Zone.BATTLEFIELD, new EldraziMimicEffect(), filter, true, SetTargetPointer.PERMANENT,
                "Whenever another colorless creature enters the battlefield under your control, you may have the base power and toughness of {this} become "
                        + "that creature's power and toughness until end of turn"));
    }

    public EldraziMimic(final EldraziMimic card) {
        super(card);
    }

    @Override
    public EldraziMimic copy() {
        return new EldraziMimic(this);
    }
}

class EldraziMimicEffect extends OneShotEffect {

    public EldraziMimicEffect() {
        super(Outcome.Detriment);
    }

    public EldraziMimicEffect(final EldraziMimicEffect effect) {
        super(effect);
    }

    @Override
    public EldraziMimicEffect copy() {
        return new EldraziMimicEffect(this);
    }

    @Override
    public boolean apply(Game game, Ability source) {
        Player controller = game.getPlayer(source.getControllerId());
        if (controller != null) {
            Permanent permanent = game.getPermanentOrLKIBattlefield(getTargetPointer().getFirst(game, source));
            if (permanent != null) {
                ContinuousEffect effect = new SetPowerToughnessSourceEffect(permanent.getPower().getValue(), permanent.getToughness().getValue(), Duration.EndOfTurn, SubLayer.SetPT_7b);
                game.addEffect(effect, source);
                return true;
            }
        }
        return false;
    }
}
