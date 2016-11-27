/*
 * $Id$
 *
 * Copyright 2012 - 2016 Allen D. Ball.  All rights reserved.
 */
package ball.game.sudoku;

import ball.annotation.ServiceProviderFor;
import ball.util.CoordinateMap;
import java.util.SortedSet;

/**
 * Sudoku simple "rule-of-sums" {@link Rule} implementation.  Calculates the
 * minimum maximum possible value for any cell and removes any greater
 * numbers from consideration.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
@ServiceProviderFor({ Rule.class })
public class RuleOfSums extends Rule {

    /**
     * Sole constructor.
     */
    public RuleOfSums() { super(); }

    @Override
    public boolean applyTo(Puzzle puzzle) {
        boolean modified = false;

        for (Cell cell : puzzle.values()) {
            if (! cell.isSolved()) {
                int max = cell.last();

                for (CoordinateMap<Cell> map : puzzle.subMapsOf(cell)) {
                    max = Math.min(max, max(map.values()));
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
