/*
 * $Id$
 *
 * Copyright 2012, 2013 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.scrabble;

/**
 * Scrabble triple letter {@link Square}.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class TL extends Square implements Cloneable {

    /**
     * Sole constructor.
     */
    public TL() { }

    @Override
    public TL clone() throws CloneNotSupportedException {
        return (TL) super.clone();
    }
}
