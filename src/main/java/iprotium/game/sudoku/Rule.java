/*
 * $Id$
 *
 * Copyright 2012 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.sudoku;

/**
 * Sudoku {@link Puzzle} solution {@link Rule}.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
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
}
