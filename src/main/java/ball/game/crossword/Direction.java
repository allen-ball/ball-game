package ball.game.crossword;
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
import ball.util.CoordinateMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

import static java.util.Collections.unmodifiableMap;

/**
 * Crossword clue {@link Direction}.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public enum Direction {
    ACROSS, DOWN;

    private static final Map<String,Direction> MAP;

    static {
        TreeMap<String,Direction> map =
            new TreeMap<>(String.CASE_INSENSITIVE_ORDER);

        Stream.of(values())
            .forEach(t -> map.put(t.name().substring(0, 1), t));

        MAP = unmodifiableMap(map);
    }

    /**
     * Static method to parse a {@link String} consistent with
     * {@link #name()} and {@link #toString()} to a {@link Direction}.
     *
     * @param   string          The {@link String} to parse.
     *
     * @return  The {@link Direction}.
     */
    public static Direction parse(String string) {
        var direction = MAP.get(string);

        if (direction == null) {
            direction = Enum.valueOf(Direction.class, string);
        }

        return direction;
    }

    /**
     * Static method to analyze {@link CoordinateMap} to determine a
     * {@link Direction}.
     *
     * @param   map             The {@link CoordinateMap} to analyze.
     *
     * @return  The {@link Direction}.
     *
     * @throws  IllegalArgumentException
     *                          If both {@link CoordinateMap#getRowCount()}
     *                          and {@link CoordinateMap#getColumnCount()}
     *                          are not equal to {@code 1}.
     */
    public static Direction of(CoordinateMap<?> map) {
        Direction direction = null;

        if (map.getRowCount() > 1) {
            direction = Direction.DOWN;
        } else if (map.getColumnCount() > 1) {
            direction = Direction.ACROSS;
        } else {
            throw new IllegalArgumentException();
        }

        return direction;
    }
}
