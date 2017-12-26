/*
 * $Id$
 *
 * Copyright 2017 Allen D. Ball.  All rights reserved.
 */
package ball.game.card.poker;

import ball.game.card.Card;
import java.util.Collection;

/**
 * Straight {@link Hand}.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class Straight extends Hand {

    /**
     * Sole constructor.
     *
     * @param   collection      The {@link Collection} of {@link Card}s to
     *                          use to attempt to form the {@link Hand}.
     */
    public Straight(Collection<Card> collection) {
        super(collection);

        if (required.isEmpty()) {
            throw new IllegalArgumentException(String.valueOf(collection));
        }
    }
}
