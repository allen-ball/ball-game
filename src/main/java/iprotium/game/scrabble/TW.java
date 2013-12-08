/*
 * $Id$
 *
 * Copyright 2012, 2013 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.scrabble;

/**
 * Scrabble triple word {@link Square}.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class TW extends Square implements Cloneable {

    /**
     * Sole constructor.
     */
    public TW() { }

    @Override
    public TW clone() throws CloneNotSupportedException {
        return (TW) super.clone();
    }
}
