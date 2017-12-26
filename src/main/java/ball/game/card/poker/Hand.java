/*
 * $Id$
 *
 * Copyright 2017 Allen D. Ball.  All rights reserved.
 */
package ball.game.card.poker;

import ball.game.card.Card;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;

/**
 * Abstract Poker {@link Hand} base class.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public abstract class Hand implements Comparable<Hand> {
    protected final List<Card> required = new ArrayList<>();
    protected final List<Card> remaining = new ArrayList<>();

    /**
     * Poker hands ranked in best-to-worse order.
     *
     * {@include.collection #TYPES}
     */
    public static final List<Class<? extends Hand>> TYPES =
        unmodifiableList(asList(FiveOfAKind.class,
                                RoyalFlush.class,
                                StraightFlush.class,
                                FourOfAKind.class,
                                FullHouse.class,
                                Flush.class,
                                Straight.class,
                                ThreeOfAKind.class,
                                TwoPair.class,
                                Pair.class,
                                HighCard.class,
                                Hand.class));

    private static final int SIZE = TYPES.size();

    private static int strengthOf(Class<? extends Hand> type) {
        return SIZE - TYPES.indexOf(type);
    }

    /**
     * Sole constructor.
     *
     * @param   collection      The {@link Collection} of {@link Card}s to
     *                          use to attempt to form the {@link Hand}.
     */
    protected Hand(Collection<Card> collection) {
        remaining.addAll(collection);
    }

    {
        for (Card card : required) {
            remaining.remove(card);
        }
    }

    /**
     * Method to get the {@link List} of {@link Card}s required for this
     * {@link Hand}.
     *
     * @return  The {@link List} of {@link Card}s.
     */
    public List<Card> getRequired() { return unmodifiableList(required); }

    @Override
    public int compareTo(Hand that) {
        int difference =
            strengthOf(this.getClass()) - strengthOf(that.getClass());

        return difference;
    }

    @Override
    public boolean equals(Object object) {
        return ((object instanceof Hand)
                    ? equals((Hand) object)
                    : super.equals(object));
    }

    private boolean equals(Hand that) {
        return (that != null && this.compareTo(that) == 0);
    }

    @Override
    public int hashCode() {
        int code = 0;

        code ^= getClass().hashCode();
/*
        if (getRank() != null) {
            code ^= getRank().hashCode();
        }
*/
        return code;
    }

    @Override
    public String toString() {
        ArrayList<Card> list = new ArrayList<>(getRequired());

        list.addAll(remaining.subList(0,
                                      Math.min(remaining.size(),
                                               5 - list.size())));

        return list.toString();
    }

    /**
     * Method to create the best possible {@link Hand} from a
     * {@link Collection} of {@link Card}s.
     *
     * @param   collection      The {@link Collection} of {@link Card}s to
     *                          use.
     *
     * @return  A {@link Hand} if one could be formed; {@code null}
     *          otherwise.
     */
    public static Hand make(Collection<Card> collection) {
        Hand hand = null;

        for (Class<? extends Hand> type : TYPES) {
            try {
                hand =
                    type
                    .getConstructor(Collection.class)
                    .newInstance(collection);
                break;
            } catch (Exception exception) {
                continue;
            }
        }

        return hand;
    }
}
