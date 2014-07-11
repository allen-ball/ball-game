/*
 * $Id$
 *
 * Copyright 2012 - 2014 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

/**
 * Scrabble double word {@link Square}.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class DW extends Square implements Cloneable {

    /**
     * Sole constructor.
     */
    public DW() { super("*"); }

    @Override
    public DW clone() throws CloneNotSupportedException {
        return (DW) super.clone();
    }
}
