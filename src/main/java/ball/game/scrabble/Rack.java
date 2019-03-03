/*
 * $Id$
 *
 * Copyright 2014 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

import ball.util.stream.Combinations;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Scrabble {@link Rack}.
 *
 * {@bean.info}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public class Rack extends ArrayList<Tile> implements Cloneable {
    private static final long serialVersionUID = 5205845897527835056L;

    private static final int CAPACITY = 7;

    /**
     * Sole constructor.
     */
    public Rack() { super(CAPACITY); }

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

        return new ArrayList<Tile>(subList(from, size()));
    }

    /**
     * Method to get the {@link Collection} of all possible combinations.
     *
     * @return  The {@link Collection} of combinations (each a {@link List}
     *          of {@link Tile}s).
     *
     * @see Combinations
     */
    public Collection<List<Tile>> combinations() {
        return Combinations.of(this, size(), 1).collect(Collectors.toList());
    }

    @Override
    public boolean add(Tile tile) {
        if (! hasCapacity()) {
            throw new IllegalStateException();
        }

        requireNonNull(tile, "tile");

        return super.add(tile);
    }

    @Override
    public Tile[] toArray() { return toArray(new Tile[] { }); }

    @Override
    public Rack clone() { return (Rack) super.clone(); }
}
