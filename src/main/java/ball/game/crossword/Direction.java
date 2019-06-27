/*
 * $Id$
 *
 * Copyright 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.crossword;

import ball.util.CoordinateMap;
import java.util.Map;
import java.util.TreeMap;

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

        for (Direction direction : values()) {
            map.put(direction.name().substring(0, 1), direction);
        }

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
        Direction direction = MAP.get(string);

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
