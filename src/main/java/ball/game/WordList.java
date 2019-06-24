/*
 * $Id$
 *
 * Copyright 2014 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game;

import java.beans.ConstructorProperties;
import java.util.Locale;
import java.util.TreeSet;

import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;

/**
 * Abstract Word List base class.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public abstract class WordList extends TreeSet<CharSequence> {
    private static final long serialVersionUID = -7167327382151879649L;

    /** @serial */ private final Locale locale;

    /**
     * Sole constructor.
     *
     * @param   locale          The {@link Locale}.
     */
    @ConstructorProperties({ "locale" })
    protected WordList(Locale locale) {
        super(comparing(t -> t.toString(), String.CASE_INSENSITIVE_ORDER));

        this.locale = requireNonNull(locale, "locale");
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
