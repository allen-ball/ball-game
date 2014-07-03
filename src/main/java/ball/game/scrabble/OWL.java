/*
 * $Id$
 *
 * Copyright 2010 - 2014 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

import ball.util.CharSequenceOrder;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TreeSet;

/**
 * Scrabble Official Word List ({@link OWL}).
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class OWL extends TreeSet<CharSequence> implements Cloneable {
    private static final long serialVersionUID = 2941475666048685465L;

    private final Locale locale;

    /**
     * No-argument constructor.
     */
    public OWL() { this(Locale.ENGLISH); }

    /**
     * @param   language        The {@link Locale} language.
     */
    public OWL(String language) { this(new Locale(language)); }

    private OWL(Locale locale) {
        super(CharSequenceOrder.CASE_INSENSITIVE);

        if (locale != null) {
            this.locale = locale;
        } else {
            throw new NullPointerException("locale");
        }

        ResourceBundle bundle =
            ResourceBundle.getBundle(getClass().getName(), getLocale());

        addAll(bundle.keySet());
    }

    /**
     * Method to get the {@link Locale} for this {@link OWL}.
     *
     * @return  The {@link Locale} for this {@link OWL}.
     */
    public Locale getLocale() { return locale; }

    @Override
    public CharSequence[] toArray() { return toArray(new CharSequence[] { }); }

    @Override
    public OWL clone() { return (OWL) super.clone(); }
}
