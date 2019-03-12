/*
 * $Id$
 *
 * Copyright 2010 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.card;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * {@link Card} color {@link Enum} type.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public enum Color {
    BLACK, RED;

    /**
     * Method to return a {@link Predicate} to test if a {@link Color} is
     * the specified {@link Color}.
     *
     * @param   color           The {@link Color}.
     *
     * @return  {@link Predicate}
     */
    public static Predicate<Color> is(Color color) {
        return t -> Objects.equals(color, t);
    }
}
