/*
 * $Id$
 *
 * Copyright 2010 - 2013 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.card;

import java.beans.ConstructorProperties;
import java.util.Collections;
import java.util.Map;
import java.util.TreeMap;

import static iprotium.game.card.Color.BLACK;
import static iprotium.game.card.Color.RED;

/**
 * {@link Card} suit {@link Enum} type.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public enum Suit {
    CLUBS(BLACK), DIAMONDS(RED), HEARTS(RED), SPADES(BLACK);

    private static final Map<String,Suit> MAP;

    static {
        TreeMap<String,Suit> map =
            new TreeMap<String,Suit>(String.CASE_INSENSITIVE_ORDER);

        for (Suit suit : Suit.values()) {
            map.put(suit.name(), suit);
            map.put(suit.toString(), suit);
        }

        MAP = Collections.unmodifiableMap(map);
    }

    private final Color color;
    private transient String string = null;

    @ConstructorProperties( { "color" } )
    private Suit(Color color) { this.color = color; }

    /**
     * Method to get the {@link Suit} {@link Color}.
     *
     * @return  {@link Color}
     */
    public Color getColor() { return color; }

    @Override
    public String toString() {
        if (string == null) {
            string = name().substring(0, 1);
        }

        return string;
    }

    /**
     * Static method to parse a {@link String} consistent with
     * {@link #name()} and {@link #toString()} to a {@link Suit}.
     *
     * @param   string          The {@link String} to parse.
     *
     * @return  The {@link Suit}.
     */
    public static Suit parse(String string) {
        Suit suit = MAP.get(string);

        if (suit == null) {
            suit = Enum.valueOf(Suit.class, string);
        }

        return suit;
    }
}
