/*
 * $Id$
 *
 * Copyright 2012, 2013 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.sudoku;

import iprotium.annotation.ServiceProviderFor;
import iprotium.game.Grid;

/**
 * Sudoku "rule-of-elimination" {@link Rule} implementation.  If a digit is
 * the solution to a {@link Cell} then it cannot be the solution to any
 * other {@link Cell} in the row, column, or 3x3 nonet.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision$
 */
@ServiceProviderFor({ Rule.class })
public class RuleOfElimination extends Rule {

    /**
     * Sole constructor.
     */
    public RuleOfElimination() { super(); }

    @Override
    public boolean applyTo(Puzzle puzzle) {
        boolean modified = false;

        for (Cell cell : puzzle) {
            if (! cell.isSolved()) {
                for (Grid<Cell> grid : puzzle.subgridsOf(cell)) {
                    modified |= cell.removeAll(used(grid));
                }
            }
        }

        return modified;
    }
}
