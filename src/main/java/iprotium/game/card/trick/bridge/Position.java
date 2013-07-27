/*
 * $Id$
 *
 * Copyright 2013 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.card.trick.bridge;

/**
 * Bridge position {@link Enum} type.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
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
        Position left = null;

        switch (right) {
        case SOUTH:
        default:
            left = WEST;
            break;

        case WEST:
            left = NORTH;
            break;

        case NORTH:
            left = EAST;
            break;

        case EAST:
            left = SOUTH;
            break;
        }

        return left;
    }
}
