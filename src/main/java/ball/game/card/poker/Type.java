/*
 * $Id$
 *
 * Copyright 2017 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.card.poker;

import ball.game.card.Card;
import ball.game.card.Cards;
import ball.util.stream.Combinations;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static ball.game.card.Cards.SAME_RANK;
import static ball.game.card.Cards.SAME_SUIT;
import static ball.game.card.Cards.SEQUENCE;
import static ball.game.card.Cards.anyMatch;
import static ball.game.card.Rank.ACE;
import static ball.game.card.Rank.KING;

/**
 * Poker hand {@link Type} {@link Enum} and {@link Predicate}.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public enum Type implements Predicate<List<Card>> {
    HighCard(0, null, 1, t -> true),
        Pair(2, SAME_RANK, 2, SAME_RANK),
        TwoPair(2, SAME_RANK, 4, Pair.with(Pair)),
        ThreeOfAKind(3, SAME_RANK, 3, SAME_RANK),
        Straight(5, SEQUENCE, 5, SEQUENCE),
        Flush(5, SAME_SUIT, 5, SAME_SUIT),
        FullHouse(3, SAME_RANK, 5, ThreeOfAKind.with(Pair)),
        FourOfAKind(4, SAME_RANK, 4, SAME_RANK),
        StraightFlush(5, SEQUENCE.and(SAME_SUIT), 5, Straight.and(Flush)),
        RoyalFlush(5, anyMatch(ACE).and(SEQUENCE).and(SAME_SUIT),
                   5, anyMatch(ACE).and(anyMatch(KING))),
        FiveOfAKind(5, SAME_RANK, 5, SAME_RANK);

    private final int prefix;
    private final Predicate<List<Card>> prerequisite;
    private final int required;
    private final Predicate<List<Card>> predicate;

    private Type(int prefix, Predicate<List<Card>> prerequisite,
                 int required, Predicate<List<Card>> predicate) {
        this.prefix = prefix;
        this.prerequisite = prerequisite;
        this.required = required;
        this.predicate = Objects.requireNonNull(predicate);
    }

    @Override
    public boolean test(List<Card> list) {
        return (list.size() >= required
                && predicate.test(list.subList(0, required)));
    }

    private Predicate<List<Card>> with(Predicate<List<Card>> that) {
        return t -> test(t) && that.test(remaining(t));
    }

    private List<Card> remaining(List<Card> list) {
        return list.subList(required, list.size());
    }

    private Predicate<List<Card>> prerequisite() {
        return t -> (t.size() > prefix
                     || (prerequisite == null || prerequisite.test(t)));
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

        Combinations.of(5, Math.min(5, collection.size()),
                        prerequisite(), collection)
            .filter(this)
            .forEach(t -> {
                         t.subList(required, t.size())
                             .sort(Comparator.reverseOrder());

                         map
                             .computeIfAbsent(new HashSet<>(t.subList(0, required)),
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
}
