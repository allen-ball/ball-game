/*
 * $Id$
 *
 * Copyright 2016 Allen D. Ball.  All rights reserved.
 */
package ball.game.sudoku;

import ball.annotation.ServiceProviderFor;
import ball.util.CoordinateMap;

import static java.util.Collections.frequency;

/**
 * Sudoku "rule-of-N-identical-cells-of-size-N" {@link Rule} implementation.
 * If a row, column, or nonet contain two cells that are identical with the
 * same two possible digits, then those digits may be removed as possible
 * solutions from the other cells in that same row, column, or nonet because
 * those two digits may only be used as a solution in those two cells.
 *
 * This idea can be extended to three identical cells of three remaining
 * options, four of four, etc...  It also works for N=1 (cell is solved).
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
@ServiceProviderFor({ Rule.class })
public class RuleOfNIdenticalCellsOfSizeN extends RuleOfElimination {

    /**
     * Sole constructor.
     */
    public RuleOfNIdenticalCellsOfSizeN() { super(); }

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
                for (CoordinateMap<Cell> map : puzzle.subMapsOf(cell)) {
                    if (frequency(map.values(), cell) == cell.size()) {
                        for (Cell other : map.values()) {
                            if (! other.equals(cell)) {
                                modified |= other.removeAll(cell);
                            }
                        }
                    }
                }
            }
        }

        return modified;
    }
}
