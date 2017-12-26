/*
 * $Id$
 *
 * Copyright 2017 Allen D. Ball.  All rights reserved.
 */
package ball.game.card.poker;

import ball.game.card.Card;
import ball.game.card.Rank;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Pair {@link Hand}.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class Pair extends Hand {

    /**
     * Sole constructor.
     *
     * @param   collection      The {@link Collection} of {@link Card}s to
     *                          use to attempt to form the {@link Hand}.
     */
    public Pair(Collection<Card> collection) {
        super(collection);

        Map<Rank,List<Card>> map = Card.sortBy(Rank.class, collection);

        for (Rank rank : map.keySet()) {
            if (map.get(rank).size() >= 2) {
                required.addAll(map.get(rank).subList(0, 2));
                break;
            }
        }

        if (required.isEmpty()) {
            throw new IllegalArgumentException(String.valueOf(collection));
        }
    }
}
