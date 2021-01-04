package ball.game.card.poker;
/*-
 * ##########################################################################
 * Game Applications and Utilities
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2010 - 2021 Allen D. Ball
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ##########################################################################
 */
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
