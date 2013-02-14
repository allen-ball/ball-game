/*
 * $Id$
 *
 * Copyright 2012, 2013 Allen D. Ball.  All rights reserved.
 */
package iprotium.game;

import iprotium.text.TableModel;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * {@link Grid} base class.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision$
 */
public class Grid<E> extends TableModel implements Iterable<E> {
    private static final long serialVersionUID = 6509895045792637390L;

    private final Class<E> type;
    private final List<List<E>> lists;
    private final List<Grid<E>> rows;
    private final List<Grid<E>> columns;

    /**
     * Sole public constructor.
     *
     * @param   height          The extent of the Y-axis.
     * @param   width           The extent of the X-axis.
     * @param   type            The {@link Class} of grid cell.
     */
    public Grid(int height, int width, Class<E> type) {
        this(height, width, type, null, -1, -1);
    }

    /**
     * Protected constructor to {@link Grid} possibly backed by another
     * {@link Grid}.
     *
     * @param   height          The extent of the Y-axis.
     * @param   width           The extent of the X-axis.
     * @param   type            The {@link Class} of grid cell.
     * @param   grid            The backing {@link Grid} (may be
     *                          {@code null}).
     * @param   y0              The Y-coordinate of the upper lefthand
     *                          corner of the sub-grid.
     * @param   x0              The X-coordinate of the upper lefthand
     *                          corner of the sub-grid.
     */
    protected Grid(int height, int width, Class<E> type,
                   Grid<E> grid, int y0, int x0) {
        super(width);

        this.type = (grid != null) ? grid.type : type;

        ArrayList<List<E>> lists = new ArrayList<List<E>>(height);

        if (grid != null) {
            for (List<? extends E> list :
                     grid.lists.subList(y0, y0 + height)) {
                lists.add(unmodifiableList(list.subList(x0, x0 + width)));
            }
        } else {
            for (int y = 0; y < height; y += 1) {
                lists.add(unmodifiableList(new Row(width, this.type)));
            }
        }

        this.lists = unmodifiableList(lists);
        this.rows = subGrids(1, getColumnCount());
        this.columns = subGrids(getRowCount(), 1);
    }

    /**
     * Method to get the sub-{@link Grid}s representing the rows.
     *
     * @return  The {@link List} of row {@link Grid}s.
     */
    public List<Grid<E>> rows() { return rows; }

    /**
     * Method to get the sub-{@link Grid}s representing the columns.
     *
     * @return  The {@link List} of column {@link Grid}s.
     */
    public List<Grid<E>> columns() { return columns; }

    /**
     * Method to get the {@link List} of sub-{@link Grid}s of with specific
     * dimensions.
     *
     * @param   height          The number of rows.
     * @param   width           The number of columns.
     *
     * @return  The {@link List} of sub-{@link Grid}s.
     */
    public List<Grid<E>> subGrids(int height, int width) {
        ArrayList<Grid<E>> list = new ArrayList<Grid<E>>();

        for (int y = 0; y < getRowCount(); y += height) {
            for (int x = 0; x < getColumnCount(); x += width) {
                list.add(subGrid(y, y + height, x, x + width));
            }
        }

        return unmodifiableList(list);
    }

    /**
     * Method to create a sub-{@link Grid} backed by {@code this}
     * {@link Grid}.
     *
     * @param   yFrom           The start of the Y-extent (inclusive).
     * @param   yTo             The end of the Y-extent (exclusive).
     * @param   xFrom           The start of the X-extent (inclusive).
     * @param   xTo             The end of the X-extent (exclusive).
     *
     * @return  The sub-{@link Grid}.
     */
    public Grid<E> subGrid(int yFrom, int yTo, int xFrom, int xTo) {
        Grid<E> grid = null;
        int height = yTo - yFrom;
        int width = xTo - xFrom;

        if (yFrom == 0 && height == getRowCount()
            && xFrom == 0 && width == getColumnCount()) {
            grid = this;
        } else {
            grid = new Sub(height, width, yFrom, xFrom);
        }

        return grid;
    }

    @Override
    public Class<? extends E> getColumnClass(int x) { return type; }

    @Override
    public int getRowCount() { return lists.size(); }

    @Override
    public E getValueAt(int y, int x) { return lists.get(y).get(x); }

    @Override
    public void setValueAt(Object value, int y, int x) {
        lists.get(y).set(x, type.cast(value));
    }

    @Override
    public Iterator<E> iterator() {
        ArrayList<E> collection =
            new ArrayList<E>(getRowCount() * getColumnCount());

        for (List<? extends E> list : lists) {
            collection.addAll(list);
        }

        return unmodifiableList(collection).iterator();
    }

    private class Row extends ArrayList<E> {
        private static final long serialVersionUID = 6737750969514493847L;

        public Row(int width, Class<? extends E> type) {
            super(width);

            try {
                for (int x = 0; x < width; x += 1) {
                    add(type.newInstance());
                }
            } catch (Exception exception) {
                throw new ExceptionInInitializerError(exception);
            }
        }
    }

    private class Sub extends Grid<E> {
        private static final long serialVersionUID = -4134129332208780443L;

        public Sub(int height, int width, int y0, int x0) {
            super(height, width, null, Grid.this, y0, x0);
        }
    }
}
