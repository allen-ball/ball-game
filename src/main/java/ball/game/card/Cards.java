/*
 * $Id$
 *
 * Copyright 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.card;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/**
 * {@link Card} static methods and fields.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public abstract class Cards {
    private Cards() { }

    /**
     * {@link Predicate} to test all {@link Card}s are the same
     * {@link Color}.
     */
    public static final Predicate<List<Card>> SAME_COLOR =
        t -> ((! t.isEmpty())
              && t.stream().allMatch(Card.isSameColorAs(t.get(0))));

    /**
     * {@link Predicate} to test all {@link Card}s are the same
     * {@link Rank}.
     */
    public static final Predicate<List<Card>> SAME_RANK =
        t -> ((! t.isEmpty())
              && t.stream().allMatch(Card.isSameRankAs(t.get(0))));

    /**
     * {@link Predicate} to test all {@link Card}s are the same
     * {@link Suit}.
     */
    public static final Predicate<List<Card>> SAME_SUIT =
        t -> ((! t.isEmpty())
              && t.stream().allMatch(Card.isSameSuitAs(t.get(0))));

    /**
     * {@link Predicate} to test the {@link Card}s make up a sequence.
     */
    public static final Predicate<List<Card>> SEQUENCE =
        t -> ((! t.isEmpty())
              && (isSequenced(t, c -> c.getRank().rank())
                  || isSequenced(t, c -> c.getRank().ordinal())));

    private static <T> boolean isSequenced(List<T> list,
                                           Function<T,Integer> mapper) {
        return (list.stream()
                .map(mapper)
                .collect(Collectors.toList())
                .equals(IntStream.iterate(list.stream()
                                          .map(mapper)
                                          .findFirst().orElse(0),
                                          t -> t - 1)
                        .limit(list.size())
                        .boxed()
                        .collect(Collectors.toList())));
    }

    /**
     * Method to return a {@link Predicate} to test if all {@link Card}s are
     * the specified {@link Color}.
     *
     * @param   color           The {@link Color}.
     *
     * @return  {@link Predicate}
     */
    public static Predicate<List<Card>> allMatch(Color color) {
        return t -> t.stream().allMatch(Card.is(color));
    }

    /**
     * Method to return a {@link Predicate} to test if any {@link Card}s are
     * the specified {@link Color}.
     *
     * @param   color           The {@link Color}.
     *
     * @return  {@link Predicate}
     */
    public static Predicate<List<Card>> anyMatch(Color color) {
        return t -> t.stream().anyMatch(Card.is(color));
    }

    /**
     * Method to return a {@link Predicate} to test if all {@link Card}s are
     * the specified {@link Rank}.
     *
     * @param   rank            The {@link Rank}.
     *
     * @return  {@link Predicate}
     */
    public static Predicate<List<Card>> allMatch(Rank rank) {
        return t -> t.stream().allMatch(Card.is(rank));
    }

    /**
     * Method to return a {@link Predicate} to test if any {@link Card}s are
     * the specified {@link Rank}.
     *
     * @param   rank            The {@link Rank}.
     *
     * @return  {@link Predicate}
     */
    public static Predicate<List<Card>> anyMatch(Rank rank) {
        return t -> t.stream().anyMatch(Card.is(rank));
    }

    /**
     * Method to return a {@link Predicate} to test if all {@link Card}s are
     * the specified {@link Suit}.
     *
     * @param   suit            The {@link Suit}.
     *
     * @return  {@link Predicate}
     */
    public static Predicate<List<Card>> allMatch(Suit suit) {
        return t -> t.stream().allMatch(Card.is(suit));
    }

    /**
     * Method to return a {@link Predicate} to test if any {@link Card}s are
     * the specified {@link Suit}.
     *
     * @param   suit            The {@link Suit}.
     *
     * @return  {@link Predicate}
     */
    public static Predicate<List<Card>> anyMatch(Suit suit) {
        return t -> t.stream().anyMatch(Card.is(suit));
    }
}
