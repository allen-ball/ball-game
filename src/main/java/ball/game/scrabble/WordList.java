/*
 * $Id$
 *
 * Copyright 2014 - 2016 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

import ball.util.CharSequenceOrder;
import java.beans.ConstructorProperties;
import java.util.Locale;
import java.util.TreeSet;

/**
 * Scrabble Official Word List ({@link WordList}).
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public abstract class WordList extends TreeSet<CharSequence> {
    private static final long serialVersionUID = 9123060041133450168L;

    private final Locale locale;

    /**
     * Sole constructor.
     *
     * @param   locale          The {@link Locale}.
     */
    @ConstructorProperties({ "locale" })
    protected WordList(Locale locale) {
        super(CharSequenceOrder.CASE_INSENSITIVE);

        if (locale != null) {
            this.locale = locale;
        } else {
            throw new NullPointerException("locale");
        }
    }

    /**
     * Method to get the {@link Locale} for this {@link WordList}.
     *
     * @return  The {@link Locale} for this {@link WordList}.
     */
    public Locale getLocale() { return locale; }

    @Override
    public CharSequence[] toArray() { return toArray(new CharSequence[] { }); }
}
