/*
 * $Id$
 *
 * Copyright 2012 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.sudoku;

import ball.util.Coordinate;
import ball.util.CoordinateMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Sudoku {@link Puzzle}.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public class Puzzle extends CoordinateMap<Cell> {
    private static final long serialVersionUID = -4650945638478115982L;

    /** @serial */ private final List<CoordinateMap<Cell>> nonets;

    /**
     * Sole constructor.
     */
    public Puzzle() {
        super(Cell.class);

        for (Coordinate key : Coordinate.range(0, 0, 9, 9)) {
            put(key, new Cell());
        }

        ArrayList<CoordinateMap<Cell>> list = new ArrayList<>();

        for (int y = 0, yN = getRowCount(); y < yN; y += 3) {
            for (int x = 0, xN = getColumnCount(); x < xN; x += 3) {
                list.add(subMap(y, x, y + 3, x + 3));
            }
        }

        nonets = Collections.unmodifiableList(list);
    }

    /**
     * Method to get the sub-{@link CoordinateMap}s representing the 3x3 boxes
     * (nonets).
     *
     * @return  The {@link List} of 3x3 nonet {@link CoordinateMap}s.
     */
    public List<CoordinateMap<Cell>> nonets() {
        return Collections.unmodifiableList(nonets);
    }

    /**
     * Method to get the sub-{@link CoordinateMap}s representing the
     * 9-{@link Cell} groups where the digits 1-9 must appear exactly once.
     * See {@link #rows()}, {@link #columns()}, and {@link #nonets()}.
     *
     * @return  The {@link List} of Sudoku sub-{@link CoordinateMap}s.
     */
    public List<CoordinateMap<Cell>> subMaps() {
        ArrayList<CoordinateMap<Cell>> list = new ArrayList<>();

        list.addAll(rows());
        list.addAll(columns());
        list.addAll(nonets());

        return list;
    }

    /**
     * Method to get the sub-{@link CoordinateMap}s representing the
     * 9-{@link Cell} groups including the argument {@link Cell}.  See
     * {@link #rows()}, {@link #columns()}, and {@link #nonets()}.
     *
     * @see #subMaps()
     *
     * @param   cell            The argument {@link Cell}.
     *
     * @return  The {@link List} of Sudoku sub-{@link CoordinateMap}s for the
     *          argument {@link Cell}.
     */
    public List<CoordinateMap<Cell>> subMapsOf(Cell cell) {
        ArrayList<CoordinateMap<Cell>> list = new ArrayList<>();

        for (CoordinateMap<Cell> map : subMaps()) {
            if (cell.isIn(map.values())) {
                list.add(map);
            }
        }

        return list;
    }

    /**
     * Method to test if the current {@link Cell} values constitute a legal
     * Sudoku puzzle.
     *
     * @return  {@code true} if {@link.this} {@link Puzzle} is legal;
     *          {@code false} otherwise.
     */
    public boolean isLegal() {
        boolean legal = true;

        for (CoordinateMap<Cell> grid : subMaps()) {
            legal &= isLegal(grid);
        }

        return legal;
    }

    private boolean isLegal(CoordinateMap<Cell> map) {
        boolean legal = true;
        Digits digits = new Digits();

        for (Cell cell : map.values()) {
            if (cell.isSolved()) {
                legal &= digits.addAll(cell);

                if (! legal) {
                    break;
                }
            }
        }

        return legal;
    }

    /**
     * Method to test if the current {@link Cell} values constitute a solved
     * Sudoku puzzle.
     *
     * @return  {@code true} if {@link.this} {@link Puzzle} is solved;
     *          {@code false} otherwise.
     */
    public boolean isSolved() {
        boolean solved = isLegal();

        for (Cell cell : values()) {
            solved &= cell.isSolved();

            if (! solved) {
                break;
            }
        }

        return solved;
    }

    /**
     * Method to apply the argument {@link Rule} to solve {@link.this}
     * {@link Puzzle}.
     *
     * @param   rule            The {@link Rule} to apply.
     *
     * @return  {@code true} if {@link Puzzle} is modified;
     *          {@code false} otherwise.
     */
    public boolean apply(Rule rule) { return rule.applyTo(this); }
}
