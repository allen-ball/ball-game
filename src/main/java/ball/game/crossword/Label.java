/*
 * $Id$
 *
 * Copyright 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.crossword;

import java.util.Comparator;
import java.util.Objects;

/**
 * Crossword clue {@link Label}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public class Label implements Comparable<Label> {
    private static final Comparator<? super Label> COMPARATOR =
        Comparator
        .<Label>comparingInt(t -> t.direction.ordinal())
        .thenComparingInt(t -> t.index);

    private final Direction direction;
    private final int index;

    /**
     * @param   direction       The solution word {@link Direction}.
     * @param   index           The starting block index.
     */
    protected Label(Direction direction, int index) {
        this.direction = Objects.requireNonNull(direction);
        this.index = index;
    }

    public Direction getDirection() { return direction; }

    public int getIndex() { return index; }

    @Override
    public int compareTo(Label that) {
        return Objects.compare(this, that, COMPARATOR);
    }

    @Override
    public boolean equals(Object object) {
        return ((object instanceof Label)
                ? (this.compareTo((Label) object) == 0)
                : super.equals(object));
    }

    @Override
    public int hashCode() { return Objects.hash(direction, index); }

    @Override
    public String toString() {
        return (getDirection().toString().substring(0, 1)
                + String.valueOf(index));
    }

    public static Label parse(String string) {
        return new Label(Direction.parse(string.substring(0, 1)),
                         Integer.parseInt(string.substring(1)));
    }
}
