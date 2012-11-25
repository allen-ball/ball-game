/*
 * $Id$
 *
 * Copyright 2012 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.sudoku;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * Sudoku {@link Digits}.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision$
 */
public class Digits extends TreeSet<Integer> {
    private static final long serialVersionUID = 7009779435461682795L;

    protected static final SortedSet<Integer> ALL;

    static {
        TreeSet<Integer> digits = new TreeSet<Integer>();

        for (int i = 1; i <= 9; i += 1) {
            digits.add(i);
        }

        ALL = Collections.unmodifiableSortedSet(digits);
    }

    /**
     * Sole constructor.
     */
    public Digits() { super(); }

    /**
     * See {@link #addAll(Collection)}.
     */
    public boolean addAll(Integer... digits) {
        return addAll(Arrays.asList(digits));
    }

    /**
     * See {@link #removeAll(Collection)}.
     */
    public boolean removeAll(Integer... digits) {
        return removeAll(Arrays.asList(digits));
    }

    /**
     * See {@link #retainAll(Collection)}.
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