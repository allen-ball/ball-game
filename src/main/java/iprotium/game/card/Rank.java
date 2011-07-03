/*
 * $Id$
 *
 * Copyright 2010, 2011 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.card;

import java.util.Arrays;
import java.util.TreeSet;

/**
 * {@link Card} rank {@link Enum} type.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision$
 */
public enum Rank {
    ACE,
        TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN,
        JACK, QUEEN, KING;

    private static final TreeSet<Rank> NAMED =
        new TreeSet<Rank>(Arrays.asList(ACE, JACK, QUEEN, KING));

    @Override
    public String toString() {
        return (NAMED.contains(this)
                    ? name().substring(0, 1)
                    : String.valueOf(ordinal() + 1));
    }
}
