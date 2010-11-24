/*
 * $Id: OWL.java,v 1.1 2010-11-24 05:17:26 ball Exp $
 *
 * Copyright 2010 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.scrabble;

import iprotium.util.Order;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TreeSet;

/**
 * Scrabble Official Word List ({@link OWL}).
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision: 1.1 $
 */
public class OWL extends TreeSet<CharSequence> {
    private static final long serialVersionUID = -3136908773362383664L;

    private static final OrderImpl ORDER = new OrderImpl();

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
        super(ORDER);

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
    public OWL clone() /* throws CloneNotSupportedException */ {
        return (OWL) super.clone();
    }

    private static class OrderImpl extends Order<CharSequence> {
        private static final long serialVersionUID = -1592933537263187812L;

        public OrderImpl() { super(); }

        @Override
        public int compare(CharSequence left, CharSequence right) {
            int difference = 0;

            if (difference == 0) {
                int n = Math.min(left.length(), right.length());

                for (int i = 0; i < n; i += 1) {
                    difference =
                        Character.toUpperCase(left.charAt(i))
                        - Character.toUpperCase(right.charAt(i));

                    if (difference != 0) {
                        break;
                    }
                }
            }

            if (difference == 0) {
                difference = left.length() - right.length();
            }

            return difference;
        }
    }
}
/*
 * $Log: not supported by cvs2svn $
 */
