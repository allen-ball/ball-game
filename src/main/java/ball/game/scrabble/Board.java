/*
 * $Id$
 *
 * Copyright 2011 - 2014 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

import ball.game.Grid;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.TreeMap;

/**
 * Scrabble {@link Board}.
 *
 * {@include Board.properties}
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class Board extends Grid<Square> {
    private static final long serialVersionUID = -399826193477575690L;

    private static final ResourceBundleMap MAP =
        new ResourceBundleMap(Board.class);

    /**
     * Sole public constructor.
     */
    public Board() { this(MAP); }

    private Board(ResourceBundleMap map) {
        super(map.size(), map.size(), Square.class, squares(map));
    }

    private static List<? extends Square> squares(ResourceBundleMap map) {
        ArrayList<Square> list = null;

        try {
            int n = map.size();

            list = new ArrayList<Square>(n * n);

            String pkg = Square.class.getPackage().getName();

            for (String value : map.values()) {
                String[] names = value.split("[\\p{Space}]+", n);

                for (int i = 0; i < n; i += 1) {
                    String name = pkg + "." + names[i];

                    try {
                        list.add((Square) Class.forName(name).newInstance());
                    } catch (Exception exception) {
                        list.add(new Square());
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
