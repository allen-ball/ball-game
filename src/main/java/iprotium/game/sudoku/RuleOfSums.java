/*
 * $Id$
 *
 * Copyright 2012 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.sudoku;

import iprotium.game.Grid;
import java.util.SortedSet;

/**
 * Sudoku simple "rule-of-sums" {@link Rule} implementation.  Calculates the
 * minimum maximum possible value for any cell and removes any greater
 * numbers from consideration.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision$
 */
public class RuleOfSums extends Rule {

    /**
     * Sole constructor.
     */
    public RuleOfSums() { super(); }

    @Override
    public boolean applyTo(Puzzle puzzle) {
        boolean modified = false;

        for (Cell cell : puzzle) {
            if (cell.size() > 1) {
                int max = Digits.ALL.last();

                for (Grid<Cell> grid : puzzle.groups()) {
                    if (contains(grid, cell)) {
                        max = Math.min(max, max(grid));
                    }
                }

                SortedSet<Integer> set = cell.tailSet(max, false);

                modified |= (! set.isEmpty());
                set.clear();
            }
        }

        return modified;
    }

    private boolean contains(Grid<? extends Object> grid, Cell cell) {
        boolean isContained = false;

        for (Object object : grid) {
            isContained |= (cell == object);

            if (isContained) {
                break;
            }
        }

        return isContained;
    }

    private int max(Grid<Cell> grid) {
        int sum = 0;
        int count = 0;

        for (Cell cell : grid) {
            if (cell.size() == 1) {
                sum += cell.first();
                count += 1;
            }
        }

        return (Digits.SUM - sum) - (Digits.ALL.size() - (count + 1));
    }
}
