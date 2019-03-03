/*
 * $Id$
 *
 * Copyright 2012 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.sudoku;

import ball.annotation.ServiceProviderFor;
import ball.util.CoordinateMap;
import java.util.SortedSet;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Sudoku simple "rule-of-sums" {@link Rule} implementation.  Calculates the
 * minimum maximum possible value for any cell and removes any greater
 * numbers from consideration.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@ServiceProviderFor({ Rule.class })
@NoArgsConstructor @ToString
public class RuleOfSums extends RuleOfElimination {
    @Override
    public boolean applyTo(Puzzle puzzle) {
        boolean modified = false;

        for (;;) {
            if (iterate(puzzle)) {
                modified |= true;
                super.applyTo(puzzle);
            } else {
                break;
            }
        }

        return modified;
    }

    private boolean iterate(Puzzle puzzle) {
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
