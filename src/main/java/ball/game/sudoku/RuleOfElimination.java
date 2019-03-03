/*
 * $Id$
 *
 * Copyright 2012 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.sudoku;

import lombok.NoArgsConstructor;
import lombok.ToString;
import ball.annotation.ServiceProviderFor;
import ball.util.CoordinateMap;

/**
 * Sudoku "rule-of-elimination" {@link Rule} implementation.  If a digit is
 * the solution to a {@link Cell} then it cannot be the solution to any
 * other {@link Cell} in the row, column, or 3x3 nonet.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@ServiceProviderFor({ Rule.class })
@NoArgsConstructor @ToString
public class RuleOfElimination extends Rule {
    @Override
    public boolean applyTo(Puzzle puzzle) {
        boolean modified = false;

        for (;;) {
            if (iterate(puzzle)) {
                modified |= true;
            } else {
                break;
            }
        }

        return modified;
    }

    private boolean iterate(Puzzle puzzle) {
        boolean modified = false;

        for (Cell cell : puzzle.values()) {
            if (cell.isSolved()) {
                for (CoordinateMap<Cell> map : puzzle.subMapsOf(cell)) {
                    for (Cell other : map.values()) {
                        if (other != cell) {
                            modified |= other.removeAll(cell);
                        }
                    }
                }
            }
        }

        return modified;
    }
}
