/*
 * $Id$
 *
 * Copyright 2010 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.card;

import java.beans.ConstructorProperties;
import java.util.Collections;
import java.util.Comparator;
import java.util.EnumSet;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
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
    public int compareTo(Card that) {
        return COMPARATOR.compare(this, that);
    }

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
     * Method to return a {@link Predicate} to test if a {@link Object} has
     * the same attribute as the specified {@link Object}.
     *
     * @param   attribute       The attribute accessor {@link Function}.
     * @param   as              The reference {@link Object}.
     * @param   <T>             The type of {@link Object}.
     * @param   <U>             The type of attribute.
     *
     * @return  {@link Predicate}
     */
    public static <T,U> Predicate<T> isSame(Function<T,U> attribute, T as) {
        return t -> Objects.equals(attribute.apply(t), attribute.apply(as));
    }

    /**
     * {@link Card} rank {@link Enum} type.
     */
    public enum Rank implements Predicate<Card> {
        JOKER,
        ACE(14),
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN,
        JACK, QUEEN, KING;

        private static final EnumSet<Rank> FACE =
            EnumSet.of(JACK, QUEEN, KING);
        private static final Map<String,Rank> MAP;

        static {
            TreeMap<String,Rank> map =
                new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

            for (Rank rank : Rank.values()) {
                map.put(rank.name(), rank);
                map.put(rank.toString(), rank);
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
         * @return      {@code rank}
         */
        public int rank() { return (rank != null) ? rank : ordinal(); }

        @Override
        public boolean test(Card card) {
            return is(this).test(card.getRank());
        }

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
         * @param       string  The {@link String} to parse.
         *
         * @return      The {@link Rank}.
         */
        public static Rank parse(String string) {
            Rank rank = MAP.get(string);

            if (rank == null) {
                rank = Enum.valueOf(Rank.class, string);
            }

            return rank;
        }

        /**
         * Method to return a {@link Predicate} to test if a {@link Rank} is
         * the specified {@link Rank}.
         *
         * @param       rank    The {@link Rank}.
         *
         * @return      {@link Predicate}
         */
        public static Predicate<Rank> is(Rank rank) {
            return t -> Objects.equals(rank, t);
        }
    }

    /**
     * {@link Card} color {@link Enum} type.
     */
    public enum Color implements Predicate<Card> {
        BLACK, RED;

        @Override
        public boolean test(Card card) {
            return is(this).test(card.getColor());
        }

        /**
         * Method to return a {@link Predicate} to test if a {@link Color} is
         * the specified {@link Color}.
         *
         * @param       color   The {@link Color}.
         *
         * @return      {@link Predicate}
         */
        public static Predicate<Color> is(Color color) {
            return t -> Objects.equals(color, t);
        }
    }

    /**
     * {@link Card} suit {@link Enum} type.
     */
    public enum Suit implements Predicate<Card> {
        CLUBS(Color.BLACK),     /* black: U+2663, white: U+2667 */
        DIAMONDS(Color.RED),    /* black: U+2666, white: U+2662 */
        HEARTS(Color.RED),      /* black: U+2665, white: U+2661 */
        SPADES(Color.BLACK);    /* black: U+2664, white: U+2660 */

        private static final Map<String,Suit> MAP;

        static {
            TreeMap<String,Suit> map =
                new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

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
         * @return      {@link Color}
         */
        public Color getColor() { return color; }

        @Override
        public boolean test(Card card) {
            return is(this).test(card.getSuit());
        }

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
         * @param       string  The {@link String} to parse.
         *
         * @return      The {@link Suit}.
         */
        public static Suit parse(String string) {
            Suit suit = MAP.get(string);

            if (suit == null) {
                suit = Enum.valueOf(Suit.class, string);
            }

            return suit;
        }

        /**
         * Method to return a {@link Predicate} to test if a {@link Suit} is
         * the specified {@link Suit}.
         *
         * @param       suit    The {@link Suit}.
         *
         * @return      {@link Predicate}
         */
        public static Predicate<Suit> is(Suit suit) {
            return t -> Objects.equals(suit, t);
        }
    }
}
