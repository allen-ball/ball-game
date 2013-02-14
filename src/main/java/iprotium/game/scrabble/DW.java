/*
 * $Id$
 *
 * Copyright 2012, 2013 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.scrabble;

/**
 * Scrabble double word {@link Square}.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision$
 */
public class DW extends Square implements Cloneable {

    /**
     * Sole constructor.
     */
    public DW() { }

    @Override
    public DW clone() throws CloneNotSupportedException {
        return (DW) super.clone();
    }
}
