/*
 * $Id$
 *
 * Copyright 2019, 2020 Allen D. Ball.  All rights reserved.
 */
package ball.game.card.poker;

import ball.game.card.Card;
import java.util.function.Predicate;

import static ball.game.card.Card.Rank.JACK;
import static ball.game.card.Card.Rank.KING;
import static ball.game.card.Card.Suit.HEARTS;
import static ball.game.card.Card.Suit.SPADES;
import static ball.game.card.Card.Suit.DIAMONDS;

/**
 * Poker-specific static fields and methods.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public abstract class Poker {
    private Poker() { }

    /**
     * {@link Predicate} to test for one-eyed
     * {@link ball.game.card.Card.Rank#JACK JACK}
     */
    public Predicate<Card> ONE_EYED_JACK = JACK.and(SPADES.or(HEARTS));

    /**
     * {@link Predicate} to test for one-eyed
     * {@link ball.game.card.Card.Rank#KING KING}
     */
    public Predicate<Card> ONE_EYED_KING = KING.and(DIAMONDS);

    /**
     * {@link Predicate} to test for "suicide"
     * {@link ball.game.card.Card.Rank#KING KING}
     */
    public Predicate<Card> SUICIDE_KING = KING.and(HEARTS);
}
