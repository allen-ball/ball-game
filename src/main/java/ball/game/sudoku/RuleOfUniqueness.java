/*
 * $Id$
 *
 * Copyright 2016 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.sudoku;

import ball.annotation.ServiceProviderFor;
import ball.util.CoordinateMap;
import java.util.TreeSet;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * Sudoku "rule-of-uniqueness" {@link Rule} implementation.  If a digit is
 * the only possible solution for a {@link Cell} in its row, column, and 3x3
 * nonet once other possible solutions for the other cells in the same row,
 * column, and nonet are removed then it must be the solution for that cell.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@ServiceProviderFor({ Rule.class })
@NoArgsConstructor @ToString
public class RuleOfUniqueness extends RuleOfElimination {
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
                TreeSet<Integer> set = new TreeSet<>();

                for (CoordinateMap<Cell> map : puzzle.subMapsOf(cell)) {
                    TreeSet<Integer> subset = new TreeSet<>(cell);

                    for (Cell other : map.values()) {
                        if (other != cell) {
                            subset.removeAll(other);
                        }
                    }

                    set.addAll(subset);
                }

                if (! set.isEmpty()) {
                    modified |= cell.retainAll(set);
                }
            }
        }

        return modified;
    }
}
