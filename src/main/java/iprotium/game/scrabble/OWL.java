/*
 * $Id: OWL.java,v 1.2 2010-11-28 00:15:52 ball Exp $
 *
 * Copyright 2010 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.scrabble;

import iprotium.util.CharSequenceOrder;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TreeSet;

/**
 * Scrabble Official Word List ({@link OWL}).
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision: 1.2 $
 */
public class OWL extends TreeSet<CharSequence> {
    private static final long serialVersionUID = 8620121775973601775L;

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
/*
 * $Log: not supported by cvs2svn $
 */
