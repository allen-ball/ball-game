/*
 * $Id$
 *
 * Copyright 2010 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.card;

import java.beans.ConstructorProperties;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.util.Arrays.asList;
import static java.util.Collections.indexOfSubList;
import static java.util.Collections.reverse;
import static java.util.Collections.unmodifiableList;
import static java.util.Collections.unmodifiableMap;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Playing {@link Card}.
 *
 * {@bean.info}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public class Card implements Comparable<Card> {
    private static final Comparator<? super Card> COMPARATOR =
        Comparator
        .<Card>comparingInt(t -> t.getSuit().ordinal())
        .thenComparingInt(t -> t.getRank().ordinal());

    private final Suit suit;
    private final Rank rank;
    private final transient String string;

    /**
     * Sole protected constructor.
     *
     * @param   suit            The {@link Card} {@link Suit}.
     * @param   rank            The {@link Card} {@link Rank}.
     */
    @ConstructorProperties({ "suit", "rank" })
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
        return Objects.compare(this, that, COMPARATOR);
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

    private static <T> Predicate<List<T>> same(Function<T,Predicate<T>> mapper) {
        return t -> ((! t.isEmpty())
                     && t.stream().allMatch(mapper.apply(t.get(0))));
    }

    private static <T,R> List<R> listOf(Collection<T> collection,
                                        Function<T,R> mapper) {
        return collection.stream().map(mapper).collect(toList());
    }

    /**
     * {@link Card} rank {@link Enum} type.
     */
    public enum Rank implements Predicate<Card> {
        JOKER,
        ACE,
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE,
        TEN, JACK, QUEEN, KING;

        private transient String string = null;

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
         * {@include #ACE_HIGH}
         */
        public static final List<Rank> ACE_HIGH  =
            unmodifiableList(asList(JOKER,
                                    TWO, THREE, FOUR, FIVE,
                                    SIX, SEVEN, EIGHT, NINE,
                                    TEN, JACK, QUEEN, KING, ACE));

        /**
         * {@include #ACE_LOW}
         */
        public static final List<Rank> ACE_LOW =
            unmodifiableList(asList(values()));

        private static final Map<String,Rank> MAP;
        private static final List<List<Rank>> SEQUENCES;

        static {
            TreeMap<String,Rank> map =
                new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

            for (Rank rank : values()) {
                map.put(rank.name(), rank);
                map.put(rank.toString(), rank);
            }

            MAP = unmodifiableMap(map);

            List<Rank> high = new ArrayList<>(Rank.ACE_HIGH);
            List<Rank> low = new ArrayList<>(Rank.ACE_LOW);

            reverse(high);
            reverse(low);

            SEQUENCES =
                unmodifiableList(asList(unmodifiableList(high),
                                        unmodifiableList(low)));
        }

        /**
         * {@link Predicate} to test all {@link Card}s are the same
         * {@link Rank}.
         */
        public static final Predicate<List<Card>> SAME = same(Card::getRank);

        /**
         * {@link Predicate} to test the {@link Card}s make up a sequence.
         */
        public static final Predicate<List<Card>> SEQUENCE =
            t -> ((! t.isEmpty()) && sequence(listOf(t, Card::getRank)));

        private static boolean sequence(List<Rank> list) {
            return (SEQUENCES.stream()
                    .anyMatch(t -> indexOfSubList(t, list) >= 0));
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
         * {@link Predicate} to test all {@link Card}s are the same
         * {@link Color}.
         */
        public static final Predicate<List<Card>> SAME = same(Card::getColor);

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
        CLUBS(Color.BLACK, "\u2667" /* U+2663 */),
        DIAMONDS(Color.RED, "\u2662" /* U+2666 */),
        HEARTS(Color.RED, "\u2661" /* U+2665 */),
        SPADES(Color.BLACK, "\u2664" /* U+2660 */);

        private static final Map<String,Suit> MAP;

        static {
            TreeMap<String,Suit> map =
                new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

            for (Suit suit : values()) {
                map.put(suit.name(), suit);
                map.put(suit.name().substring(0, 1), suit);
                map.put(suit.toString(), suit);
            }

            MAP = unmodifiableMap(map);
        }

        private final Color color;
        private final String string;

        @ConstructorProperties({ "color", EMPTY })
        private Suit(Color color, String string) {
            this.color = color;
            this.string = string;
        }

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
        public String toString() { return string; }

        /**
         * {@link Predicate} to test all {@link Card}s are the same
         * {@link Suit}.
         */
        public static final Predicate<List<Card>> SAME = same(Card::getSuit);

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
    }
}
