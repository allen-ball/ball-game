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
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PROTECTED;

/**
 * Sudoku {@link Puzzle} solution {@link Rule}.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@NoArgsConstructor(access = PROTECTED)
public abstract class Rule {

    /**
     * Method to apply {@link.this} {@link Rule} to the argument
     * {@link Puzzle}.
     *
     * @param   puzzle          The {@link Puzzle} to solve.
     *
     * @return  {@code true} if {@link.this} {@link Puzzle} is modified;
     *          {@code false} otherwise.
     */
    public abstract boolean applyTo(Puzzle puzzle);

    /**
     * Method to get the count of solved {@link Cell}s.
     *
     * @param   iterable        The {@link Iterable} of {@link Cell}s.
     *
     * @return  The count of solved {@link Cell}s.
     */
    protected int count(Iterable<Cell> iterable) {
        int count = 0;

        for (Cell cell : iterable) {
            if (cell.isSolved()) {
                count += 1;
            }
        }

        return count;
    }

    /**
     * Method to get the sum of solved {@link Cell}s.
     *
     * @param   iterable        The {@link Iterable} of {@link Cell}s.
     *
     * @return  The sum of solved {@link Cell}s.
     */
    protected int sum(Iterable<Cell> iterable) {
        int sum = 0;

        for (Cell cell : iterable) {
            if (cell.isSolved()) {
                sum += cell.solution();
            }
        }

        return sum;
    }

    /**
     * Method to get the solved {@link Digits}.
     *
     * @param   iterable        The {@link Iterable} of {@link Cell}s.
     *
     * @return  The solved {@link Digits}.
     */
    protected Digits solved(Iterable<Cell> iterable) {
        Digits digits = new Digits();

        digits.clear();

        for (Cell cell : iterable) {
            if (cell.isSolved()) {
                digits.addAll(cell);
            }
        }

        return digits;
    }
}
