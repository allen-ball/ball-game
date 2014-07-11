/*
 * $Id$
 *
 * Copyright 2010 - 2014 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * Scrabble {@link Bag}.
 *
 * {@bean-info}
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class Bag extends ArrayList<Tile> implements Cloneable {
    private static final long serialVersionUID = -3450364322503748772L;

    private final Locale locale;
    private final Map<Character,Integer> frequencies;
    private final Map<Character,Integer> points;

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

        frequencies = Collections.unmodifiableMap(new Frequencies());
        points = Collections.unmodifiableMap(new Points());

        for (Character key : frequencies.keySet()) {
            for (int i = 0, n = frequencies.get(key); i < n; i += 1) {
                add(new Tile(key, points.get(key)));
            }
        }
    }

    /**
     * Method to get the {@link Locale} for this {@link Bag}.
     *
     * @return  The {@link Locale} for this {@link Bag}.
     */
    public Locale getLocale() { return locale; }

    /**
     * Method to get the frequency {@link Map} for this {@link Bag}.
     *
     * @return  The frequency {@link Map} for this {@link Bag}.
     */
    public Map<Character,Integer> frequencies() { return frequencies; }

    /**
     * Method to get the points {@link Map} for this {@link Bag}.
     *
     * @return  The points {@link Map} for this {@link Bag}.
     */
    public Map<Character,Integer> points() { return points; }

    @Override
    public Tile[] toArray() { return toArray(new Tile[] { }); }

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
