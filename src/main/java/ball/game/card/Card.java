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
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.regex.Pattern;

/**
 * Playing card.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class Card implements Comparable<Card> {
    private static final Comparator<Card> COMPARATOR =
        Comparator
        .<Card>comparingInt(t -> t.getSuit().ordinal())
        .thenComparing(t -> t.getRank());

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
     * Static method to get the {@link List} of {@link Card}s that have the
     * matching attribute.
     *
     * @param   attribute       The {@link Enum}.
     * @param   collection      The {@link Collection} of {@link Card}s to
     *                          examine.
     *
     * @return  The {@link List} of matching {@link Card}s.
     */
    public static List<Card> get(Enum<?> attribute,
                                 Collection<Card> collection) {
        ArrayList<Card> list = new ArrayList<>();

        for (Card card : collection) {
            if (attribute instanceof Color) {
                if (attribute.equals(card.getColor())) {
                    list.add(card);
                }
            } else if (attribute instanceof Rank) {
                if (attribute.equals(card.getRank())) {
                    list.add(card);
                }
            } else if (attribute instanceof Suit) {
                if (attribute.equals(card.getSuit())) {
                    list.add(card);
                }
            }
        }

        return list;
    }

    /**
     * Static method to sort the {@link Collection} of {@link Card}s by an
     * attribute.
     *
     * @param   type            The atribute {@link Enum} {@link Class}.
     * @param   collection      The {@link Collection} of {@link Card}s to
     *                          examine.
     *
     * @param   <E>             The attribute {@link Enum} generic type.
     *
     * @return  The {@link SortedMap} of sorted {@link Card}s.
     */
    public static <E extends Enum<E>> SortedMap<E,List<Card>> sortBy(Class<E> type,
                                                                     Collection<Card> collection) {
        TreeMap<E,List<Card>> map = new TreeMap<>();

        for (E attribute : type.getEnumConstants()) {
            map.put(attribute, get(attribute, collection));
        }

        return map;
    }
}
