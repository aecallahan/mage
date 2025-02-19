package mage.game.permanent.token;

import mage.MageInt;
import mage.constants.CardType;
import mage.constants.SubType;

import java.util.Arrays;

public final class OozeToken extends TokenImpl {

    public OozeToken(int power, int toughness) {
        super("Ooze Token", power + "/" + toughness + " green ooze creature token");
        cardType.add(CardType.CREATURE);
        color.setGreen(true);
        subtype.add(SubType.OOZE);
        this.power = new MageInt(power);
        this.toughness = new MageInt(toughness);
    }

    public OozeToken() {
        super("Ooze Token", "X/X green ooze creature token");
        cardType.add(CardType.CREATURE);
        color.setGreen(true);
        subtype.add(SubType.OOZE);
        power = new MageInt(0);
        toughness = new MageInt(0);

        availableImageSetCodes = Arrays.asList("ALA", "ROE", "RTR", "MM3", "UMA", "GK2", "2XM");
    }

    public OozeToken(final OozeToken token) {
        super(token);
    }

    public OozeToken copy() {
        return new OozeToken(this);
    }
}
