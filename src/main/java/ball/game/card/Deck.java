package ball.game.card;
/*-
 * ##########################################################################
 * Game Applications and Utilities
 * %%
 * Copyright (C) 2010 - 2022 Allen D. Ball
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
import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * {@link Card} deck.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 */
public abstract class Deck extends ArrayList<Card> implements Cloneable {
    private static final long serialVersionUID = -1376087186450102030L;

    /**
     * Sole constructor.
     */
    protected Deck() {
        super(Card.Suit.values().length * Card.Rank.values().length);

        try {
            var bundle = ResourceBundle.getBundle(getClass().getName());

            for (var key : bundle.keySet()) {
                var value = bundle.getString(key);

                if (! StringUtils.isEmpty(value)) {
                    for (var suit : key.split(Pattern.quote(","))) {
                        for (var rank : value.split(Pattern.quote(","))) {
                            add(new Card(Card.Suit.parse(suit), Card.Rank.parse(rank)));
                        }
                    }
                } else {
                    add(Card.parse(key));
                }
            }
        } catch (Exception exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    @Override
    public Card[] toArray() { return toArray(new Card[] { }); }

    @Override
    public Deck clone() { return (Deck) super.clone(); }
}
