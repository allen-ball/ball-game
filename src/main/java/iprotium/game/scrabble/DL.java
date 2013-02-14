/*
 * $Id$
 *
 * Copyright 2012, 2013 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.scrabble;

/**
 * Scrabble double letter {@link Square}.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision$
 */
public class DL extends Square implements Cloneable {

    /**
     * Sole constructor.
     */
    public DL() { }

    @Override
    public DL clone() throws CloneNotSupportedException {
        return (DL) super.clone();
    }
}
