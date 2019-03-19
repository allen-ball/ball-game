/*
 * $Id$
 *
 * Copyright 2017 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.card.poker;

import ball.game.card.Card.Rank;
import ball.game.card.Card.Suit;
import ball.game.card.Card;
import ball.util.stream.Combinations;
import ball.util.stream.Permutations;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
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

    private final int size;
    private final Predicate<List<Card>> possible;
    private final Predicate<List<Card>> is;

    private Ranking(int size,
                    Predicate<List<Card>> possible, Predicate<List<Card>> is) {
        this.size = size;
        this.possible = possible;
        this.is = Objects.requireNonNull(is);
    }

    private Predicate<List<Card>> possible() {
        return t -> (possible == null || possible.test(subListTo(t, size)));
    }

    @Override
    public boolean test(List<Card> list) {
        return (list.size() >= size && is.test(subListTo(list, size)));
    }

    private Predicate<List<Card>> with(Predicate<List<Card>> that) {
        return t -> test(t) && that.test(subListFrom(t, size));
    }

    /**
     * Method that provides all the matches for {@link.this}
     * {@link Predicate}.
     *
     * @param   collection      The {@link Collection} of {@link Card}s to
     *                          analyze.
     *
     * @return  The {@link List} of matching combinations.
     */
    public List<List<Card>> matches(Collection<Card> collection) {
        HashMap<Set<Card>,Set<List<Card>>> map = new HashMap<>();
        int size = Math.min(5, collection.size());

        Combinations.of(size, size, possible(), collection)
            .filter(this)
            .forEach(t -> {
                         subListFrom(t, size).sort(Comparator.reverseOrder());

                         map
                             .computeIfAbsent(new HashSet<>(subListTo(t, size)),
                                              k -> new HashSet<>())
                             .add(t);
                     });

        List<List<Card>> list =
            map.values()
            .stream()
            .flatMap(t -> t.stream())
            .collect(Collectors.toList());

        return list;
    }

    /**
     * Static method to analyze a {@link Collection} of {@link Card}s to
     * determine the best poker hand.
     *
     * @param   collection      The {@link Collection} of {@link Card}s to
     *                          analyze.
     *
     * @return  The best {@link Ranking}.
     */
    public static Ranking evaluate(Collection<Card> collection) {
        List<Ranking> types = Stream.of(values()).collect(Collectors.toList());

        Collections.reverse(types);

        Optional<Ranking> type =
            types.stream()
            .filter(t -> Permutations.of(collection).anyMatch(t))
            .findFirst();

        return type.orElseThrow(IllegalStateException::new);
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
}
