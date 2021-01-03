package ball.game.scrabble;
/*-
 * ##########################################################################
 * Game Applications and Utilities
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2010 - 2021 Allen D. Ball
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
import java.util.LinkedList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.SortedMap;
import java.util.TreeMap;

import static java.util.Collections.unmodifiableSortedMap;
import static java.util.Objects.requireNonNull;

/**
 * Scrabble {@link Bag}.
 *
 * {@bean.info}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public class Bag extends LinkedList<Tile> implements Cloneable {
    private static final long serialVersionUID = -1363859449887620815L;

    /** @serial */ private final Locale locale;
    /** @serial */ private final SortedMap<Character,Integer> frequencies;
    /** @serial */ private final SortedMap<Character,Integer> points;

    /**
     * No-argument constructor.
     */
    public Bag() { this(Locale.ENGLISH); }

    /**
     * @param   language        The {@link Locale} language.
     */
    public Bag(String language) { this(new Locale(language)); }

    private Bag(Locale locale) {
        super();

        this.locale = requireNonNull(locale, "locale");

        frequencies = unmodifiableSortedMap(new Frequencies());
        points = unmodifiableSortedMap(new Points());

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
     * Method to get the frequency {@link SortedMap} for this {@link Bag}.
     *
     * @return  The frequency {@link SortedMap} for this {@link Bag}.
     */
    public SortedMap<Character,Integer> frequencies() { return frequencies; }

    /**
     * Method to get the points {@link SortedMap} for this {@link Bag}.
     *
     * @return  The points {@link SortedMap} for this {@link Bag}.
     */
    public SortedMap<Character,Integer> points() { return points; }

    /**
     * Method to draw the next {@link Tile} in the {@link Bag}.
     *
     * @return  The next {@link Tile}.
     *
     * @see #pollFirst()
     */
    public Tile draw() { return pollFirst(); }

    /**
     * Method to draw a specific {@link Tile} from the {@link Bag}.
     *
     * @param   letter          The name of the requested {@link Tile}.
     *
     * @return  The requested {@link Tile}; {@code null} if no matching
     *          {@link Tile} is available.
     *
     * @see #remove(int)
     */
    public Tile draw(char letter) {
        int index = -1;

        for (int i = 0, n = size(); i < n; i += 1) {
            if (get(i).getLetter() == letter) {
                index = i;
                break;
            }
        }

        return (0 <= index && index < size()) ? remove(index) : null;
    }

    @Override
    public Tile[] toArray() { return toArray(new Tile[] { }); }

    @Override
    public Bag clone() { return (Bag) super.clone(); }

    private abstract class MapImpl extends TreeMap<Character,Integer> {
        private static final long serialVersionUID = -9083183924608405152L;

        protected MapImpl() { super(); }

        protected void load(String name) {
            ResourceBundle bundle =
                ResourceBundle.getBundle(name, getLocale());

            for (String key : bundle.keySet()) {
                put(key.charAt(0), Integer.valueOf(bundle.getString(key)));
            }
        }
    }

    private class Frequencies extends MapImpl implements Cloneable {
        private static final long serialVersionUID = -5133821366028022851L;

        public Frequencies() {
            super();

            load(getClass().getName());
        }

        @Override
        public Frequencies clone() { return (Frequencies) super.clone(); }
    }

    private class Points extends MapImpl implements Cloneable {
        private static final long serialVersionUID = 6515810713447725344L;

        public Points() {
            super();

            load(getClass().getName());
        }

        @Override
        public Points clone() { return (Points) super.clone(); }
    }
}
