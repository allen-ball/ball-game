/*
 * $Id$
 *
 * Copyright 2012 - 2014 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

/**
 * Scrabble double letter square.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class DL extends SQ implements Cloneable {

    /**
     * Sole constructor.
     */
    public DL() { super("+"); }

    @Override
    public DL clone() throws CloneNotSupportedException {
        return (DL) super.clone();
    }
}
