/*
 * $Id$
 *
 * Copyright 2017 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.card.poker;

import ball.game.card.Card;
import ball.game.card.Cards;
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
import static ball.game.card.Cards.SAME_RANK;
import static ball.game.card.Cards.SAME_SUIT;
import static ball.game.card.Cards.SEQUENCE;

/**
 * Poker hand {@link Type} {@link Enum} and {@link Predicate}.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public enum Type implements Predicate<List<Card>> {
    Empty(0, null, Collection::isEmpty),
        HighCard(1, t -> true, t -> true),
        Pair(2, SAME_RANK, SAME_RANK),
        TwoPair(4, has(2, SAME_RANK), Pair.with(Pair)),
        ThreeOfAKind(3, SAME_RANK, SAME_RANK),
        Straight(5, SEQUENCE, SEQUENCE),
        Flush(5, SAME_SUIT, SAME_SUIT),
        FullHouse(5, has(3, SAME_RANK), ThreeOfAKind.with(Pair)),
        FourOfAKind(4, SAME_RANK, SAME_RANK),
        StraightFlush(5,
                      matches(ACE, KING).negate().and(SEQUENCE).and(SAME_SUIT),
                      matches(ACE, KING).negate().and(Straight).and(Flush)),
        RoyalFlush(5,
                   matches(ACE, KING).and(SEQUENCE).and(SAME_SUIT),
                   matches(ACE, KING).and(Straight).and(Flush)),
        FiveOfAKind(5, SAME_RANK, SAME_RANK);

    private final int size;
    private final Predicate<List<Card>> possible;
    private final Predicate<List<Card>> predicate;

    private Type(int size,
                 Predicate<List<Card>> possible,
                 Predicate<List<Card>> predicate) {
        this.size = size;
        this.possible = possible;
        this.predicate = Objects.requireNonNull(predicate);
    }

    private Predicate<List<Card>> possible() {
        return t -> (possible == null || possible.test(subListTo(t, size)));
    }

    @Override
    public boolean test(List<Card> list) {
        return (list.size() >= size && predicate.test(subListTo(list, size)));
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
     * @return  The best {@link Type}.
     */
    public static Type evaluate(Collection<Card> collection) {
        List<Type> types = Stream.of(values()).collect(Collectors.toList());

        Collections.reverse(types);

        Optional<Type> type =
            types.stream()
            .filter(t -> Permutations.of(collection).anyMatch(t))
            .findFirst();

        return type.orElseThrow(IllegalStateException::new);
    }

    private static <T> Predicate<List<T>> has(int count,
                                              Predicate<List<T>> predicate) {
        return t -> (t.isEmpty()
                     || predicate.test(t.subList(0,
                                                 Math.min(count, t.size()))));
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    private static <T> Predicate<List<T>> matches(Predicate<T>... array) {
        return matches(Stream.of(array).collect(Collectors.toList()));
    }

    private static <T> Predicate<List<T>> matches(List<Predicate<T>> list) {
        return t -> ((list.isEmpty() || t.isEmpty())
                     || (list.get(0).test(t.get(0))
                         && (matches(subListFrom(list, 1))
                             .test(subListFrom(t, 1)))));
    }

    private static <T> List<T> subListTo(List<T> list, int to) {
        return list.subList(0, Math.min(0, list.size()));
    }

    private static <T> List<T> subListFrom(List<T> list, int from) {
        return list.subList(from, list.size());
    }
}
