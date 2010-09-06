/*
 * $Id: Suit.java,v 1.1 2010-09-06 06:12:37 ball Exp $
 *
 * Copyright 2010 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.card;

import static iprotium.game.card.Color.BLACK;
import static iprotium.game.card.Color.RED;

/**
 * {@link Card} suit {@link Enum} type.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision: 1.1 $
 */
public enum Suit {
    CLUBS(BLACK), DIAMONDS(RED), HEARTS(RED), SPADES(BLACK);

    private final Color color;

    private Suit(Color color) { this.color = color; }

    /**
     * Method to get the {@link Suit} {@link Color}.
     *
     * @return  {@link Color}
     */
    public Color getColor() { return color; }

    @Override
    public String toString() { return name().substring(0, 1); }
}
/*
 * $Log: not supported by cvs2svn $
 */
