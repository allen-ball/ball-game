/*
 * $Id$
 *
 * Copyright 2014 - 2016 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

import ball.util.Combinations;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * Scrabble {@link Rack}.
 *
 * {@bean.info}
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
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
        LinkedList<List<Tile>> collection = new LinkedList<>();

        for (int count = size(); count > 0; count -= 1) {
            for (List<Tile> list : new Combinations<Tile>(this, count)) {
                collection.add(list);
            }
        }

        return collection;
    }

    @Override
    public boolean add(Tile tile) {
        if (! hasCapacity()) {
            throw new IllegalStateException();
        }

        if (tile == null) {
            throw new NullPointerException();
        }

        return super.add(tile);
    }

    @Override
    public Tile[] toArray() { return toArray(new Tile[] { }); }

    @Override
    public Rack clone() { return (Rack) super.clone(); }
}
