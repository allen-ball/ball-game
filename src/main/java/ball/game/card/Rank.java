/*
 * $Id$
 *
 * Copyright 2010 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.card;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Predicate;

/**
 * {@link Card} rank {@link Enum} type.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public enum Rank {
    JOKER,
    ACE(14),
    TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN,
    JACK, QUEEN, KING;

    private static final Map<String,Rank> MAP;

    static {
        TreeMap<String,Rank> map =
            new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        for (Rank suit : Rank.values()) {
            map.put(suit.name(), suit);
            map.put(suit.toString(), suit);
        }

        MAP = Collections.unmodifiableMap(map);
    }

    private Rank(Integer rank) { this.rank = rank; }
    private Rank() { this(null); }

    private final Integer rank;
    private transient String string = null;

    /**
     * Method to get this {@link Rank} rank (separate from
     * {@link #ordinal()}).
     *
     * @return  {@code rank}
     */
    public int rank() { return (rank != null) ? rank : ordinal(); }

    @Override
    public String toString() {
        if (string == null) {
            switch (this) {
            case JOKER:
                string = super.toString();
                break;

            case ACE:
            case JACK:
            case QUEEN:
            case KING:
                string = name().substring(0, 1);
                break;

            default:
                string = String.valueOf(ordinal());
                break;
            }
        }

        return string;
    }

    /**
     * Static method to parse a {@link String} consistent with
     * {@link #name()} and {@link #toString()} to a {@link Rank}.
     *
     * @param   string          The {@link String} to parse.
     *
     * @return  The {@link Rank}.
     */
    public static Rank parse(String string) {
        Rank suit = MAP.get(string);

        if (suit == null) {
            suit = Enum.valueOf(Rank.class, string);
        }

        return suit;
    }

    /**
     * Method to return a {@link Predicate} to test if a {@link Rank} is
     * the specified {@link Rank}.
     *
     * @param   rank            The {@link Rank}.
     *
     * @return  {@link Predicate}
     */
    public static Predicate<Rank> is(Rank rank) {
        return t -> Objects.equals(rank, t);
    }
}
