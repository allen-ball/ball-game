/*
 * $Id$
 *
 * Copyright 2014 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

import ball.util.stream.Combinations;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

/**
 * Scrabble {@link Rack}.
 *
 * {@bean.info}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public class Rack extends LinkedList<Tile> implements Cloneable {
    private static final long serialVersionUID = -888967675416820573L;

    private static final int CAPACITY = 7;

    /**
     * Sole constructor.
     */
    public Rack() { super(); }

    /**
     * Method to determine if the {@link Rack} has any remaining capacity
     * (should draw).
     *
     * @return  {@code true} if the {@link Rack} contains less than its
     *          maximum capacity; {@code false} otherwise.
     */
    public boolean hasCapacity() { return (size() < CAPACITY); }

    /**
     * Method to draw {@link Tile}s from a {@link Bag}.
     *
     * @param   bag             The {@link Bag} from which to draw.
     *
     * @return  The {@link List} of {@link Tile}s drawn.
     */
    public List<Tile> draw(Bag bag) {
        int from = size();

        synchronized (bag) {
            while (hasCapacity() && (! bag.isEmpty())) {
                add(bag.draw());
            }
        }

        return new LinkedList<Tile>(subList(from, size()));
    }

    /**
     * Method to get the {@link Stream} of all possible combinations.
     *
     * @return  The {@link Stream} of combinations (each a {@link List}
     *          of {@link Tile}s).
     *
     * @see Combinations
     */
    public Stream<List<Tile>> combinations() {
        return Combinations.of(size(), 1, null, this);
    }

    @Override
    public boolean add(Tile tile) {
        if (! hasCapacity()) {
            throw new IllegalStateException();
        }

        requireNonNull(tile);

        return super.add(tile);
    }

    @Override
    public Tile[] toArray() { return toArray(new Tile[] { }); }

    @Override
    public Rack clone() { return (Rack) super.clone(); }
}
