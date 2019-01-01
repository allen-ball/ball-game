/*
 * $Id$
 *
 * Copyright 2010 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.card;

import java.util.ArrayList;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

/**
 * {@link Card} deck.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public abstract class Deck extends ArrayList<Card> implements Cloneable {
    private static final long serialVersionUID = -1376087186450102030L;

    /**
     * Sole constructor.
     */
    protected Deck() {
        super(Suit.values().length * Rank.values().length);

        try {
            ResourceBundle bundle =
                ResourceBundle.getBundle(getClass().getName());

            for (String key : bundle.keySet()) {
                String value = bundle.getString(key);

                if (! StringUtils.isEmpty(value)) {
                    for (String suit : key.split(Pattern.quote(","))) {
                        for (String rank : value.split(Pattern.quote(","))) {
                            add(new Card(Suit.parse(suit), Rank.parse(rank)));
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
