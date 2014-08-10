/*
 * $Id$
 *
 * Copyright 2012 - 2014 Allen D. Ball.  All rights reserved.
 */
package ball.game;

import ball.swing.table.AbstractTableModelImpl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * Abstract {@link Grid} base class.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public abstract class Grid<E> extends AbstractTableModelImpl
                                      implements Iterable<E> {
    private final Class<E> type;
    private final List<List<E>> lists;
    private final List<Grid<E>> rows;
    private final List<Grid<E>> columns;

    /**
     * Construct a {@link Grid} of the specified type.
     *
     * @param   height          The extent of the Y-axis.
     * @param   width           The extent of the X-axis.
     * @param   type            The {@link Class} of grid cell.
     */
    protected Grid(int height, int width, Class<E> type) {
        this(height, width, type, null);
    }

    /**
     * Construct a {@link Grid} and populate with the specified values.
     *
     * @param   height          The extent of the Y-axis.
     * @param   width           The extent of the X-axis.
     * @param   type            The {@link Class} of grid cell.
     * @param   collection      The {@link Collection} of grid cell values.
     */
    protected Grid(int height, int width,
                   Class<E> type, Collection<? extends E> collection) {
        this(height, width, type, collection, null, -1, -1);
    }

    private Grid(int height, int width,
                 Class<E> type, Collection<? extends E> collection,
                 Grid<E> grid, int y0, int x0) {
        super(width);

        if (type != null) {
            this.type = type;
        } else {
            throw new NullPointerException("type");
        }

        ArrayList<List<E>> lists = new ArrayList<List<E>>(height);

        if (grid != null) {
            for (List<? extends E> list :
                     grid.lists.subList(y0, y0 + height)) {
                lists.add(unmodifiableList(list.subList(x0, x0 + width)));
            }
        } else if (collection != null) {
            Iterator<? extends E> iterator = collection.iterator();

            for (int y = 0; y < height; y += 1) {
                lists.add(unmodifiableList(new Row(width, iterator)));
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
     * Method to get the {@link List} of {@link Grid} values in row-column
     * order.
     *
     * @return  The {@link List} of {@link Grid} values in row-column
     *          order.
     */
    public List<E> asList() {
        ArrayList<E> list = new ArrayList<E>(getRowCount() * getColumnCount());

        for (List<? extends E> row : lists) {
            list.addAll(row);
        }

        return list;
    }

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
        return unmodifiableList(asList()).iterator();
    }

    @Override
    public String toString() { return lists.toString(); }

    private class Row extends ArrayList<E> {
        private static final long serialVersionUID = -5170419862060802136L;

        public Row(int width, Iterator<? extends E> iterator) {
            super(width);

            try {
                for (int x = 0; x < width; x += 1) {
                    add(iterator.next());
                }
            } catch (Exception exception) {
                throw new ExceptionInInitializerError(exception);
            }
        }

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
        private static final long serialVersionUID = 2190407932893826313L;

        public Sub(int height, int width, int y0, int x0) {
            super(height, width, Grid.this.type, null, Grid.this, y0, x0);
        }
    }
}
