/*
 * $Id$
 *
 * Copyright 2012 - 2016 Allen D. Ball.  All rights reserved.
 */
package ball.game.sudoku;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Sudoku {@link Digits}.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class Digits extends TreeSet<Integer> {
    private static final long serialVersionUID = 3540373718233836695L;

    protected static final SortedSet<Integer> ALL;
    protected static final int SUM;

    static {
        TreeSet<Integer> digits = new TreeSet<>();
        int sum = 0;

        for (int i = 1; i <= 9; i += 1) {
            digits.add(i);
            sum += i;
        }

        ALL = Collections.unmodifiableSortedSet(digits);
        SUM = sum;
    }

    /**
     * Sole constructor.
     */
    public Digits() { super(); }

    /**
     * See {@link #addAll(Collection)}.
     *
     * @param   digits          The digits to add.
     *
     * @return  {@code true} if {@code this} {@link Digits} changes;
     *          {@code false} otherwise.
     */
    public boolean addAll(Integer... digits) {
        return Collections.addAll(this, digits);
    }

    /**
     * See {@link #removeAll(Collection)}.
     *
     * @param   digits          The digits to remove.
     *
     * @return  {@code true} if {@code this} {@link Digits} changes;
     *          {@code false} otherwise.
     */
    public boolean removeAll(Integer... digits) {
        return removeAll(Arrays.asList(digits));
    }

    /**
     * See {@link #retainAll(Collection)}.
     *
     * @param   digits          The digits to retain.
     *
     * @return  {@code true} if {@code this} {@link Digits} changes;
     *          {@code false} otherwise.
     */
    public boolean retainAll(Integer... digits) {
        return retainAll(Arrays.asList(digits));
    }

    @Override
    public boolean add(Integer digit) {
        if (! ALL.contains(digit)) {
            throw new IllegalArgumentException(String.valueOf(digit));
        }

        return super.add(digit);
    }

    @Override
    public boolean addAll(Collection<? extends Integer> collection) {
        boolean modified = super.addAll(collection);

        if (retainAll(ALL)) {
            throw new IllegalArgumentException(String.valueOf(collection));
        }

        return modified;
    }
}
