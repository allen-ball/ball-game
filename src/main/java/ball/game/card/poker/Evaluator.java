/*
 * $Id$
 *
 * Copyright 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.card.poker;

import ball.game.card.Card.Rank;
import ball.game.card.Card;
import ball.util.Comparators;
import ball.util.stream.Combinations;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Poker hand {@link Evaluator}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public class Evaluator implements Predicate<List<Card>>, Consumer<List<Card>> {

    /**
     * {@link Card} {@link Comparator}
     */
    public static final Comparator<Card> CARD =
        Comparator.comparing(t -> t.getRank(),
                             Comparators.orderedBy(Rank.ACE_HIGH));

    /**
     * Hand ({@link Card} {@link List}) {@link Comparator}
     */
    public static final Comparator<List<Card>> HAND =
        (l, r) -> (IntStream.range(0, Math.min(l.size(), r.size()))
                   .map(t -> CARD.compare(l.get(t), r.get(t)))
                   .filter(t -> t != 0)
                   .findFirst().orElse(Integer.compare(l.size(), r.size())));

    private final List<Card> hand;
    private final List<Ranking> orBetter;
    private Ranking ranking = Ranking.Empty;
    private List<Card> scoring = Collections.emptyList();

    /**
     * Sole public constructor.
     *
     * @param   collection      The {@link Collection} of {@link Card}s to
     *                          evaluate.
     */
    public Evaluator(Collection<Card> collection) {
        this(collection, Ranking.values());
    }

    /**
     * Protected constructor to search for specific {@link Ranking}(s).
     *
     * @param   collection      The {@link Collection} of {@link Card}s to
     *                          evaluate.
     * @param   rankings        The {@link Ranking}s to look for.
     */
    protected Evaluator(Collection<Card> collection, Ranking... rankings) {
        hand = new ArrayList<>(collection);
        hand.sort(CARD.reversed());

        orBetter = new ArrayList<>(Arrays.asList(rankings));
        Collections.reverse(orBetter);

        int size = Math.min(5, hand.size());

        orBetter.removeAll(orBetter.stream()
                           .filter(t -> t.required() > size)
                           .collect(Collectors.toSet()));

        Combinations.of(size, size, this, hand)
            .forEach(this);

        for (int i = 0, n = scoring.size(); i < n; i += 1) {
            Collections.swap(hand, i, hand.indexOf(scoring.get(i)));
        }

        hand.subList(scoring.size(), hand.size()).sort(CARD.reversed());
    }

    /**
     * Method to get this hand as an unmodifiable {@link List} sorted
     * according to its {@link Ranking}.
     *
     * @return  The sorted {@link List}.
     */
    public List<Card> getHand() { return Collections.unmodifiableList(hand); }

    /**
     * Method to get this hand's {@link Ranking}.
     *
     * @return  The {@link Ranking}.
     */
    public Ranking getRanking() { return ranking; }

    /**
     * Method to get this hand's scoring {@link Card}s as an unmodifiable
     * {@link List}.
     *
     * @return  The {@link List} of scoring {@link Card}s.
     */
    public List<Card> getScoring() {
        return Collections.unmodifiableList(scoring);
    }

    @Override
    public boolean test(List<Card> prefix) {
        return (orBetter.stream()
                .anyMatch(t -> t.possible().test(prefix)));
    }

    @Override
    public void accept(List<Card> list) {
        Ranking ranking =
            orBetter.stream()
            .filter(t -> t.test(list))
            .findFirst().orElse(Ranking.Empty);

        List<Card> scoring = list.subList(0, ranking.required());
        int comparison = Ranking.COMPARATOR.compare(ranking, this.ranking);

        if (comparison > 0) {
            this.ranking = ranking;
            this.scoring = scoring;

            int index = orBetter.indexOf(ranking);

            if (! (index < 0)) {
                orBetter.subList(index + 1, orBetter.size()).clear();
            }
        } else if (comparison == 0) {
            if (HAND.compare(scoring, this.scoring) > 0) {
                this.scoring = scoring;
            }
        }
    }

    @Override
    public String toString() {
        return getRanking().name() + ":" + getScoring() + orBetter;
    }
}
