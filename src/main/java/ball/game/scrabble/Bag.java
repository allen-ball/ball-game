/*
 * $Id$
 *
 * Copyright 2010 - 2014 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * Scrabble {@link Bag}.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class Bag extends ArrayList<Character> implements Cloneable {
    private static final long serialVersionUID = -6473916460700746587L;

    private final Locale locale;
    private final Frequencies frequencies;
    private final Points points;

    /**
     * No-argument constructor.
     */
    public Bag() { this(Locale.ENGLISH); }

    /**
     * @param   language        The {@link Locale} language.
     */
    public Bag(String language) { this(new Locale(language)); }

    private Bag(Locale locale) {
        super(100);

        if (locale != null) {
            this.locale = locale;
        } else {
            throw new NullPointerException("locale");
        }

        frequencies = new Frequencies();
        points = new Points();

        for (Character key : frequencies.keySet()) {
            for (int i = 0, n = frequencies.get(key); i < n; i += 1) {
                add(key);
            }
        }
    }

    /**
     * Method to get the {@link Locale} for this {@link Bag}.
     *
     * @return  The {@link Locale} for this {@link Bag}.
     */
    public Locale getLocale() { return locale; }

    @Override
    public Character[] toArray() { return toArray(new Character[] { }); }

    @Override
    public Bag clone() { return (Bag) super.clone(); }

    private abstract class TreeMapImpl extends TreeMap<Character,Integer> {
        protected TreeMapImpl() { super(); }

        protected void load(String name) {
            ResourceBundle bundle =
                ResourceBundle.getBundle(name, getLocale());

            for (String key : bundle.keySet()) {
                put(key.charAt(0), Integer.valueOf(bundle.getString(key)));
            }
        }
    }

    private class Frequencies extends TreeMapImpl implements Cloneable {
        private static final long serialVersionUID = -5133821366028022851L;

        public Frequencies() {
            super();

            load(getClass().getName());
        }

        @Override
        public Frequencies clone() { return (Frequencies) super.clone(); }
    }

    private class Points extends TreeMapImpl implements Cloneable {
        private static final long serialVersionUID = 6515810713447725344L;

        public Points() {
            super();

            load(getClass().getName());
        }

        @Override
        public Points clone() { return (Points) super.clone(); }
    }
}
