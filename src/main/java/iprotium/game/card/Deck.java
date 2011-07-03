/*
 * $Id$
 *
 * Copyright 2010, 2011 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.card;

import java.util.ArrayList;

/**
 * {@link Card} deck.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision$
 */
public class Deck extends ArrayList<Card> {
    private static final long serialVersionUID = -8837506005707422876L;

    /**
     * Sole constructor.
     */
    public Deck() {
        super(52);

        for (Suit suit : Suit.values()) {
            for (Rank rank : Rank.values()) {
                add(new Card(suit, rank));
            }
        }
    }

    @Override
    public Card[] toArray() { return toArray(new Card[] { }); }

    @Override
    public Deck clone() { return (Deck) super.clone(); }
}
