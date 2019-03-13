/*
 * $Id$
 *
 * Copyright 2010 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.card;

import java.beans.ConstructorProperties;
import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

/**
 * Playing {@link Card}.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public class Card implements Comparable<Card> {
    private static final Comparator<? super Card> COMPARATOR =
        Comparator
        .<Card>comparingInt(t -> t.getSuit().ordinal())
        .thenComparingInt(t -> t.getRank().rank());

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
    public int compareTo(Card that) { return COMPARATOR.compare(this, that); }

    @Override
    public boolean equals(Object object) {
        return ((object instanceof Card)
                    ? (this.compareTo((Card) object) == 0)
                    : super.equals(object));
    }

    @Override
    public int hashCode() { return Objects.hash(getSuit(), getRank()); }

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

    /**
     * Method to return a {@link Predicate} to test if a {@link Card} is
     * the specified {@link Color}.
     *
     * @param   color           The {@link Color}.
     *
     * @return  {@link Predicate}
     */
    public static Predicate<Card> is(Color color) {
        return t -> Color.is(color).test(t.getColor());
    }

    /**
     * Method to return a {@link Predicate} to test if a {@link Card} is
     * the specified {@link Rank}.
     *
     * @param   rank            The {@link Rank}.
     *
     * @return  {@link Predicate}
     */
    public static Predicate<Card> is(Rank rank) {
        return t -> Rank.is(rank).test(t.getRank());
    }

    /**
     * Method to return a {@link Predicate} to test if a {@link Card} is
     * the specified {@link Suit}.
     *
     * @param   suit            The {@link Suit}.
     *
     * @return  {@link Predicate}
     */
    public static Predicate<Card> is(Suit suit) {
        return t -> Suit.is(suit).test(t.getSuit());
    }

    /**
     * Method to return a {@link Predicate} to test if a {@link Card} is
     * the same {@link Color} as the specified {@link Card}.
     *
     * @param   card            The {@link Card}.
     *
     * @return  {@link Predicate}
     */
    public static Predicate<Card> isSameColorAs(Card card) {
        return isSame(Card::getColor, card);
    }

    /**
     * Method to return a {@link Predicate} to test if a {@link Card} is
     * the same {@link Rank} as the specified {@link Card}.
     *
     * @param   card            The {@link Card}.
     *
     * @return  {@link Predicate}
     */
    public static Predicate<Card> isSameRankAs(Card card) {
        return isSame(Card::getRank, card);
    }

    /**
     * Method to return a {@link Predicate} to test if a {@link Card} is
     * the same {@link Suit} as the specified {@link Card}.
     *
     * @param   card            The {@link Card}.
     *
     * @return  {@link Predicate}
     */
    public static Predicate<Card> isSameSuitAs(Card card) {
        return isSame(Card::getSuit, card);
    }

    /**
     * Method to return a {@link Predicate} to test if a {@link Card} has
     * the same attribute as the specified {@link Card}.
     *
     * @param   attribute       The attribute accessor {@link Function}.
     * @param   as              The reference {@link Object}.
     *
     * @return  {@link Predicate}
     */
    public static <T,U> Predicate<T> isSame(Function<T,U> attribute, T as) {
        return t -> Objects.equals(accessor.apply(t), accessor.apply(as));
    }
}
