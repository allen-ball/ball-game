/*
 * $Id$
 *
 * Copyright 2011 - 2016 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

import ball.util.CoordinateMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * Scrabble {@link Board}.
 *
 * {@include Board.properties}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public class Board extends CoordinateMap<SQ> {
    private static final long serialVersionUID = -399826193477575690L;

    private static final ResourceBundleMap MAP =
        new ResourceBundleMap(Board.class);

    /**
     * Sole public constructor.
     */
    public Board() { this(MAP); }

    private Board(ResourceBundleMap map) {
        super(SQ.class, map.size(), map.size());

        Collections.copy(asList(), squares(map));
    }

    private static List<? extends SQ> squares(ResourceBundleMap map) {
        ArrayList<SQ> list = null;

        try {
            int n = map.size();

            list = new ArrayList<SQ>(n * n);

            String pkg = SQ.class.getPackage().getName();

            for (String value : map.values()) {
                String[] names = value.split("[\\p{Space}]+", n);

                for (int i = 0; i < n; i += 1) {
                    String name = pkg + "." + names[i];

                    try {
                        list.add((SQ) Class.forName(name).newInstance());
                    } catch (Exception exception) {
                        list.add(new SQ());
                    }
                }
            }
        } catch (Exception exception) {
            throw new ExceptionInInitializerError(exception);
        }

        return list;
    }

    private static class ResourceBundleMap extends TreeMap<String,String> {
        private static final long serialVersionUID = 5871194943402587641L;

        public ResourceBundleMap(Class<? extends Board> type) {
            super();

            ResourceBundle bundle = ResourceBundle.getBundle(type.getName());

            for (String key : bundle.keySet()) {
                put(key, bundle.getString(key).trim());
            }
        }
    }
}
