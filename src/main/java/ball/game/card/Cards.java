/*
 * $Id$
 *
 * Copyright 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.card;

import java.util.Collection;
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
     * {@link Card.Color}.
     */
    public static final Predicate<List<Card>> SAME_COLOR =
        same(Card::getColor);

    /**
     * {@link Predicate} to test all {@link Card}s are the same
     * {@link Card.Rank}.
     */
    public static final Predicate<List<Card>> SAME_RANK = same(Card::getRank);

    /**
     * {@link Predicate} to test all {@link Card}s are the same
     * {@link Card.Suit}.
     */
    public static final Predicate<List<Card>> SAME_SUIT = same(Card::getSuit);

    /**
     * {@link Predicate} to test the {@link Card}s make up a sequence.
     */
    public static final Predicate<List<Card>> SEQUENCE =
        t -> ((! t.isEmpty())
              && (isSequence(t, c -> c.getRank().rank())
                  || isSequence(t, c -> c.getRank().ordinal())));

    private static <T> boolean isSequence(Collection<T> collection,
                                          Function<T,Integer> mapper) {
        return (collection.stream()
                .map(mapper)
                .collect(Collectors.toList())
                .equals(IntStream.iterate(collection.stream()
                                          .map(mapper)
                                          .findFirst().orElse(0),
                                          t -> t - 1)
                        .limit(collection.size())
                        .boxed()
                        .collect(Collectors.toList())));
    }

    /**
     * Method to return a {@link Predicate} to test if all {@link Object}s
     * match the specified {@link Predicate} associated with the first
     * member of the list.
     *
     * @param   mapper          The mapper {@link Function}.
     * @param   <T>             The type of {@link List} element.
     *
     * @return  {@link Predicate}
     */
    public static <T> Predicate<List<T>> same(Function<T,Predicate<T>> mapper) {
        return t -> ((! t.isEmpty())
                     && t.stream().allMatch(mapper.apply(t.get(0))));
    }
}
