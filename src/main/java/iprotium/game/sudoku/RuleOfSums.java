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
                int max = cell.last();

                for (Grid<Cell> grid : puzzle.subgridsOf(cell)) {
                    max = Math.min(max, max(grid));
                }

                SortedSet<Integer> set = cell.tailSet(max, false);

                modified |= (! set.isEmpty());
                set.clear();
            }
        }

        return modified;
    }

    protected int max(Iterable<Cell> iterable) {
        return ((Digits.SUM - sum(iterable))
                - (Digits.ALL.size() - (count(iterable) + 1)));
    }
}
