package ball.game.card.trick.bridge;
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

/**
 * Bridge position {@link Enum} type.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public enum Position {
    SOUTH, WEST, NORTH, EAST;

    /**
     * Method to determine the {@link Position} to the left of the argument
     * {@link Position}.
     *
     * @param   right           The right {@link Position}.
     *
     * @return  The {@link Position} to the left.
     */
    public static Position leftOf(Position right) {
        var values = values();

        return values[(right.ordinal() + 1) % values.length];
    }

    /**
     * Method to determine the {@link Position} to the right of the argument
     * {@link Position}.
     *
     * @param   left            The left {@link Position}.
     *
     * @return  The {@link Position} to the left.
     */
    public static Position rightOf(Position left) {
        var values = values();

        return values[(left.ordinal() + values.length - 1) % values.length];
    }
}
