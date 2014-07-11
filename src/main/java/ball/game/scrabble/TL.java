/*
 * $Id$
 *
 * Copyright 2012 - 2014 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

/**
 * Scrabble triple letter square.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class TL extends SQ implements Cloneable {

    /**
     * Sole constructor.
     */
    public TL() { super("%"); }

    @Override
    public TL clone() throws CloneNotSupportedException {
        return (TL) super.clone();
    }
}
