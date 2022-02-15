package ball.game.sudoku;
/*-
 * ##########################################################################
 * Game Applications and Utilities
 * %%
 * Copyright (C) 2010 - 2022 Allen D. Ball
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
 */
@ServiceProviderFor({ Rule.class })
@NoArgsConstructor @ToString
public class RuleOfUniqueness extends RuleOfElimination {
    @Override
    public boolean applyTo(Puzzle puzzle) {
        var modified = false;

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
                TreeSet<Integer> set = new TreeSet<>();

                for (CoordinateMap<Cell> map : puzzle.subMapsOf(cell)) {
                    TreeSet<Integer> subset = new TreeSet<>(cell);

                    for (var other : map.values()) {
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
