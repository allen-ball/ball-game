package ball.game.life;
/*-
 * ##########################################################################
 * Game Applications and Utilities
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2010 - 2020 Allen D. Ball
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
import java.beans.ConstructorProperties;
import java.math.BigInteger;

/**
 * Life {@link Automata}.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public class Automata {
    private final int height;
    private final int width;

    /**
     * @param   height          The extent of the y-axis.
     * @param   width           The extent of the x-axis.
     */
    @ConstructorProperties({ "height", "width" })
    public Automata(int height, int width) {
        if (height > 0) {
            this.height = height;
        } else {
            throw new IllegalArgumentException("height=" + height);
        }

        if (width > 0) {
            this.width = width;
        } else {
            throw new IllegalArgumentException("width=" + width);
        }
    }

    /**
     * Method to get the height of the board.
     *
     * @return  The height.
     */
    public int getHeight() { return height; }

    /**
     * Method to get the width of the board.
     *
     * @return  The width.
     */
    public int getWidth() { return width; }

    /**
     * Given the current {@code state}, calculate the next {@code state}.
     *
     * @param   current         The current {@code state}.
     *
     * @return  The next {@code state}.
     */
    public BigInteger next(BigInteger current) {
        BigInteger next = BigInteger.ZERO;

        for (int y = 0; y < height; y += 1) {
            for (int x = 0; x < width; x += 1) {
                int count = 0;

                count += get(current, y - 1, x - 1) ? 1 : 0;
                count += get(current, y - 1, x)     ? 1 : 0;
                count += get(current, y - 1, x + 1) ? 1 : 0;
                count += get(current, y,     x - 1) ? 1 : 0;
                count += get(current, y,     x + 1) ? 1 : 0;
                count += get(current, y + 1, x - 1) ? 1 : 0;
                count += get(current, y + 1, x)     ? 1 : 0;
                count += get(current, y + 1, x + 1) ? 1 : 0;

                if (get(current, y, x)) {
                    switch (count) {
                    case 2:
                    case 3:
                        next = next.setBit(y * width + x);
                        break;

                    default:
                        next = next.clearBit(y * width + x);
                        break;
                    }
                } else {
                    switch (count) {
                    case 3:
                        next = next.setBit(y * width + x);
                        break;

                    default:
                        next = next.clearBit(y * width + x);
                        break;
                    }
                }
            }
        }

        return next;
    }

    /**
     * Method to get the value of a cell at a specified {@code (y, x)}
     * coordinate fron the argument {@code state}.
     *
     * @param   state           The {@link Automata} {@code state}.
     * @param   y               The {@code y} coordinate.
     * @param   x               The {@code x} coordinate.
     *
     * @return  {@code true} if the {@code (y, x)} coordinate is valid and
     *          if the specified cell is "alive;" {@code false} otherwise.
     */
    public boolean get(BigInteger state, int y, int x) {
        return ((0 <= y && y < height && 0 <= x && x < width)
                && state.testBit(y * width + x));
    }

    @Override
    public String toString() { return super.toString(); }
}
