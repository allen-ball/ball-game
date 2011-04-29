/*
 * $Id: Square.java,v 1.1 2011-04-29 02:43:19 ball Exp $
 *
 * Copyright 2011 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.scrabble;

/**
 * Scrabble {@link Board} {@link Square}.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision: 1.1 $
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
/*
 * $Log: not supported by cvs2svn $
 */
