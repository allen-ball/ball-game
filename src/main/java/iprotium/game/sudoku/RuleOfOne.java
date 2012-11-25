/*
 * $Id$
 *
 * Copyright 2012 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.sudoku;

import iprotium.game.Grid;

/**
 * Sudoku "rule-of-one" {@link Rule} implementation.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision$
 */
public class RuleOfOne extends Rule {

    /**
     * Sole constructor.
     */
    public RuleOfOne() { super(); }

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
