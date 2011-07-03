/*
 * $Id$
 *
 * Copyright 2011 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.scrabble;

/**
 * Scrabble {@link Board} {@link Square}.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision$
 */
public class Square implements Cloneable {

    /**
     * Sole constructor.
     */
    public Square() { }

    @Override
    public Square clone() {
        Square square = null;

        try {
            square = (Square) super.clone();
        } catch (CloneNotSupportedException exception) {
	    throw new InternalError();
        }

        return square;
    }
}
