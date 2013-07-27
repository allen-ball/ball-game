/*
 * $Id$
 *
 * Copyright 2010 - 2013 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.card;

import iprotium.util.ComparableUtil;
import java.beans.ConstructorProperties;
import java.util.regex.Pattern;

/**
 * Playing card.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision$
 */
public class Card implements Comparable<Card> {
    private final Suit suit;
    private final Rank rank;
    private final transient String string;

    /**
     * Sole protected constructor.
     *
     * @param   suit            The {@link Card} {@link Suit}.
     * @param   rank            The {@link Card} {@link Rank}.
     */
    @ConstructorProperties( { "suit", "rank" } )
    protected Card(Suit suit, Rank rank) {
        this.rank = rank;
        this.suit = suit;

        if (rank.equals(Rank.JOKER)) {
            if (suit != null) {
                throw new IllegalArgumentException("suit=" + suit +
                                                   ",rank=" + rank);
            }
        }

        switch (rank) {
        case JOKER:
            this.string = rank.toString();
            break;

        default:
            this.string = rank.toString() + "-" + suit.toString();
            break;
        }
    }

    /**
     * Method to get the {@link Card} {@link Suit}.
     *
     * @return  {@link Suit}
     */
    public Suit getSuit() { return suit; }

    /**
     * Method to get the {@link Card} {@link Rank}.
     *
     * @return  {@link Rank}
     */
    public Rank getRank() { return rank; }

    /**
     * Method to get the {@link Card} {@link Color}.
     *
     * @return  {@link Suit#getColor()}
     */
    public Color getColor() {
        Suit suit = getSuit();

        return (suit != null) ? suit.getColor() : null;
    }

    @Override
    public int compareTo(Card that) {
        int difference = 0;

        if (difference == 0) {
            difference =
                ComparableUtil.compare(this.getSuit(), that.getSuit());
        }

        if (difference == 0) {
            difference =
                ComparableUtil.compare(this.getRank(), that.getRank());
        }

        return difference;
    }

    @Override
    public boolean equals(Object object) {
        return ((object instanceof Card)
                    ? equals((Card) object)
                    : super.equals(object));
    }

    private boolean equals(Card that) {
        return (that != null && this.compareTo(that) == 0);
    }

    @Override
    public int hashCode() {
        int code = 0;

        if (getSuit() != null) {
            code ^= getSuit().hashCode();
        }

        if (getRank() != null) {
            code ^= getRank().hashCode();
        }

        return code;
    }

    @Override
    public String toString() { return string; }

    /**
     * Static method to parse a {@link String} consistent with
     * {@link #toString} to a {@link Card}.
     *
     * @param   string          The {@link String} to parse.
     *
     * @return  The {@link Card}.
     */
    public static Card parse(String string) {
        Card card = null;

        try {
            String[] substrings = string.split(Pattern.quote("-"), 2);

            card =
                new Card((substrings.length > 1)
                             ? Suit.parse(substrings[1])
                             : null,
                         Rank.parse(substrings[0]));
        } catch (Exception exception) {
            throw new IllegalArgumentException(string, exception);
        }

        return card;
    }
}
