/*
 * $Id$
 *
 * Copyright 2012 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.sudoku;

import java.util.Arrays;
import java.util.Collection;

/**
 * Sudoku {@link Cell}.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision$
 */
public class Cell extends Digits {
    private static final long serialVersionUID = 4667182121117647576L;

    /**
     * {@link #UNKNOWN} = {@value #UNKNOWN}
     */
    public static final String UNKNOWN = ".";

    /**
     * Default constructor (all possible digits).
     */
    public Cell() { this(ALL); }

    /**
     * Construct with specified digits.
     *
     * @param   digits          The initial digits.
     */
    public Cell(Integer... digits) { this(Arrays.asList(digits)); }

    /**
     * Construct with specified digits.
     *
     * @param   collection      The {@link Collection} of initial digits.
     */
    public Cell(Collection<? extends Integer> collection) {
        super();

        addAll(collection);
    }

    @Override
    public String toString() {
        return (size() == 1) ? String.valueOf(first()) : UNKNOWN;
    }
}
