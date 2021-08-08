package ball.game.sudoku;
/*-
 * ##########################################################################
 * Game Applications and Utilities
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2010 - 2021 Allen D. Ball
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ##########################################################################
 */
import ball.annotation.ServiceProviderFor;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@ServiceProviderFor({ Rule.class })
@NoArgsConstructor @ToString
public class RuleOfNIdenticalCellsOfSizeN extends RuleOfElimination {
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
        var modified = false;

        for (var cell : puzzle.values()) {
            if (! cell.isSolved()) {
                for (var map : puzzle.subMapsOf(cell)) {
                    if (frequency(map.values(), cell) == cell.size()) {
                        for (var other : map.values()) {
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
