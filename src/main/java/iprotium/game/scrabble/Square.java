/*
 * $Id$
 *
 * Copyright 2011 - 2013 Allen D. Ball.  All rights reserved.
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
    public Square clone() throws CloneNotSupportedException {
        return (Square) super.clone();
    }
}
