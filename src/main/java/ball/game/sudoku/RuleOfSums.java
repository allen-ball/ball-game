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
