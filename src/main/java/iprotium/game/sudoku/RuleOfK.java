/*
 * $Id$
 *
 * Copyright 2012 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.sudoku;

import iprotium.game.Grid;

/**
 * Sudoku "rule-of-K" {@link Rule} implementation.  If there are {@code k}
 * {@link Cell}s contained entirely in a region that contain exactly
 * {@code k} different possible values, then no other {@link Cell} in that
 * region can contain any of those {@code k} values.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision$
 */
public class RuleOfK extends Rule {

    /**
     * Sole constructor.
     */
    public RuleOfK() { super(); }

    @Override
    public boolean applyTo(Puzzle puzzle) {
        boolean modified = false;

        for (Grid<Cell> grid : puzzle.groups()) {
            modified |= applyTo(grid);
        }

        return modified;
    }

    private boolean applyTo(Grid<Cell> grid) {
        boolean modified = false;
        Digits digits = new Digits();

        for (Cell cell : grid) {
            if (cell.size() == 1) {
                digits.addAll(cell);
            }
        }

        for (Cell cell : grid) {
            if (cell.size() != 1) {
                modified |= cell.removeAll(digits);
            }
        }

        return modified;
    }
}
