/*
 * $Id$
 *
 * Copyright 2012 - 2014 Allen D. Ball.  All rights reserved.
 */
package ball.game.sudoku;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Sudoku {@link Puzzle} solution {@link Rule}.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public abstract class Rule {

    /**
     * Sole constructor.
     */
    protected Rule() { }

    /**
     * Method to apply {@code this} {@link Rule} to the argument
     * {@link Puzzle}.
     *
     * @param   puzzle          The {@link Puzzle} to solve.
     *
     * @return  {@code true} if {@code this} {@link Puzzle} is modified;
     *          {@code false} otherwise.
     */
    public abstract boolean applyTo(Puzzle puzzle);

    /**
     * Method to get the count of solved {@link Cell}s.
     *
     * @param   iterable        The {@link Iterable} of {@link Cell}s.
     *
     * @return  The count of solved {@link Cell}s.
     */
    protected int count(Iterable<Cell> iterable) {
        int count = 0;

        for (Cell cell : iterable) {
            if (cell.isSolved()) {
                count += 1;
            }
        }

        return count;
    }

    /**
     * Method to get the sum of solved {@link Cell}s.
     *
     * @param   iterable        The {@link Iterable} of {@link Cell}s.
     *
     * @return  The sum of solved {@link Cell}s.
     */
    protected int sum(Iterable<Cell> iterable) {
        int sum = 0;

        for (Cell cell : iterable) {
            if (cell.isSolved()) {
                sum += cell.solution();
            }
        }

        return sum;
    }

    /**
     * Method to get the {@link SortedSet} of used digits.
     *
     * @param   iterable        The {@link Iterable} of {@link Cell}s.
     *
     * @return  The {@link SortedSet} of used digits.
     */
    protected SortedSet<Integer> used(Iterable<Cell> iterable) {
        TreeSet<Integer> set = new TreeSet<>();

        for (Cell cell : iterable) {
            if (cell.isSolved()) {
                set.addAll(cell);
            }
        }

        return set;
    }

    /**
     * Method to get the {@link SortedSet} of unused digits.
     *
     * @param   iterable        The {@link Iterable} of {@link Cell}s.
     *
     * @return  The {@link SortedSet} of unused digits.
     */
    protected SortedSet<Integer> unused(Iterable<Cell> iterable) {
        TreeSet<Integer> set = new TreeSet<>(Digits.ALL);

        set.removeAll(used(iterable));

        return set;
    }

    @Override
    public String toString() { return getClass().getSimpleName(); }
}
