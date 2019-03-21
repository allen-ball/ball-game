/*
 * $Id$
 *
 * Copyright 2017 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.card.poker;

import ball.game.card.Card.Rank;
import ball.game.card.Card.Suit;
import ball.game.card.Card;
import ball.util.Comparators;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ball.game.card.Card.Rank.ACE;
import static ball.game.card.Card.Rank.KING;
import static ball.game.card.Card.Rank.SEQUENCE;

/**
 * Poker hand {@link Ranking} {@link Enum} and {@link Predicate}.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public enum Ranking implements Predicate<List<Card>> {
    Empty(0, null, Collection::isEmpty),
        HighCard(1, t -> true, t -> true),
        Pair(2, Rank.SAME, Rank.SAME),
        TwoPair(4, holding(2, Rank.SAME), Pair.with(Pair)),
        ThreeOfAKind(3, Rank.SAME, Rank.SAME),
        Straight(5, SEQUENCE, SEQUENCE),
        Flush(5, Suit.SAME, Suit.SAME),
        FullHouse(5, holding(3, Rank.SAME), ThreeOfAKind.with(Pair)),
        FourOfAKind(4, Rank.SAME, Rank.SAME),
        StraightFlush(5,
                      holding(ACE, KING).negate().and(SEQUENCE).and(Suit.SAME),
                      holding(ACE, KING).negate().and(Straight).and(Flush)),
        RoyalFlush(5,
                   holding(ACE, KING).and(SEQUENCE).and(Suit.SAME),
                   holding(ACE, KING).and(Straight).and(Flush)),
        FiveOfAKind(5, Rank.SAME, Rank.SAME);

    private final int required;
    private final Predicate<List<Card>> possible;
    private final Predicate<List<Card>> is;

    private Ranking(int required,
                    Predicate<List<Card>> possible, Predicate<List<Card>> is) {
        this.required = required;
        this.possible = possible;
        this.is = Objects.requireNonNull(is);
    }

    /**
     * Method to find the best possible {@link.this} {@link Ranking} hand in
     * the {@link Collection}.
     *
     * @param   collection      The {@link Collection} of {@link Card}s to
     *                          evaluate.
     *
     * @return  The best sorted hand as a {@link List} of {@link Card}s if
     *          a combination mathiching {@link.this} {@link Ranking} is
     *          found; the empty {@link List} otherwise.
     */
    public List<Card> find(Collection<Card> collection) {
        Evaluator evaluator = new Evaluator(collection, this);
        List<Card> hand =
            evaluator.getScoring().isEmpty()
                ? evaluator.getScoring()
                : evaluator.getHand();

        return hand;
    }

    /**
     * Returns the number of {@link Card} for {@link.this} {@link Ranking}.
     *
     * @return  The number of {@link Card}s required.
     */
    public int required() { return required; }

    /**
     * Method to return a {@link Predicate} to test if the {@link List} of
     * {@link Card} is a possible {@link Ranking}.
     *
     * @return  A {@link Predicate} that returns {@code false} if the hand
     *          cannot be {@link.this} {@link Ranking}; {@code true}
     *          otherwise.
     */
    public Predicate<List<Card>> possible() {
        return t -> (possible == null
                     || possible.test(subListTo(t, required())));
    }

    @Override
    public boolean test(List<Card> list) {
        return (list.size() >= required()
                && is.test(subListTo(list, required())));
    }

    private Predicate<List<Card>> with(Predicate<List<Card>> that) {
        return t -> test(t) && that.test(subListFrom(t, required()));
    }

    private static <T> Predicate<List<T>> holding(int count,
                                                  Predicate<List<T>> predicate) {
        return t -> (t.isEmpty() || predicate.test(subListTo(t, count)));
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    private static <T> Predicate<List<T>> holding(Predicate<T>... array) {
        return holding(Stream.of(array).collect(Collectors.toList()));
    }

    private static <T> Predicate<List<T>> holding(List<Predicate<T>> list) {
        return t -> ((list.isEmpty() || t.isEmpty())
                     || (list.get(0).test(t.get(0))
                         && (holding(subListFrom(list, 1))
                             .test(subListFrom(t, 1)))));
    }

    private static <T> List<T> subListTo(List<T> list, int to) {
        return list.subList(0, Math.min(to, list.size()));
    }

    private static <T> List<T> subListFrom(List<T> list, int from) {
        return list.subList(from, list.size());
    }

    /**
     * {@link Comparator} that orders {@link Ranking}s weakest to
     * strongest.
     */
    public static Comparator<Ranking> COMPARATOR =
        Comparators.orderedBy(Arrays.asList(values()));
}
