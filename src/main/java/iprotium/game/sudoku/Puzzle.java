/*
 * $Id$
 *
 * Copyright 2012 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.sudoku;

import iprotium.game.Grid;
import java.util.ArrayList;
import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * Sudoku {@link Puzzle}.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision$
 */
public class Puzzle extends Grid<Cell> {
    private static final long serialVersionUID = 6358701373909975308L;

    private final List<Grid<Cell>> nonets;

    /**
     * Sole constructor.
     */
    public Puzzle() {
        super(9, 9, Cell.class);

        this.nonets = subGrids(3, 3);
    }

    /**
     * Method to get the sub-{@link Grid}s representing the 3x3 boxes
     * (nonets).
     *
     * @return  The {@link List} of 3x3 nonet {@link Grid}s.
     */
    public List<Grid<Cell>> nonets() { return nonets; }

    /**
     * Method to get the sub-{@link Grid}s representing the 9-{@link Cell}
     * groups where the digits 1-9 must appear exactly once.  See
     * {@link #rows()}, {@link #columns()}, and {@link #nonets()}.
     *
     * @return  The {@link List} of Sudoku sub-{@link Grid}s.
     */
    public List<Grid<Cell>> subgrids() {
        ArrayList<Grid<Cell>> list = new ArrayList<Grid<Cell>>();

        list.addAll(rows());
        list.addAll(columns());
        list.addAll(nonets());

        return unmodifiableList(list);
    }

    /**
     * Method to get the sub-{@link Grid}s representing the 9-{@link Cell}
     * groups including the argument {@link Cell}.  See {@link #rows()},
     * {@link #columns()}, and {@link #nonets()}.
     *
     * @see #subgrids()
     *
     * @param   cell            The argument {@link Cell}.
     *
     * @return  The {@link List} of Sudoku sub-{@link Grid}s for the
     *          argument {@link Cell}.
     */
    public List<Grid<Cell>> subgridsOf(Cell cell) {
        ArrayList<Grid<Cell>> list = new ArrayList<Grid<Cell>>();

        for (Grid<Cell> grid : subgrids()) {
            if (cell.isIn(grid)) {
                list.add(grid);
            }
        }

        return unmodifiableList(list);
    }

    /**
     * Method to test if the current {@link Cell} values constitute a legal
     * Sudoku puzzle.
     *
     * @return  {@code true} if {@code this} {@link Puzzle} is legal;
     *          {@code false} otherwise.
     */
    public boolean isLegal() {
        boolean legal = true;

        for (Grid<Cell> grid : subgrids()) {
            legal &= isLegal(grid);
        }

        return legal;
    }

    private boolean isLegal(Grid<Cell> grid) {
        boolean legal = true;
        Digits digits = new Digits();

        for (Cell cell : grid) {
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
     * @return  {@code true} if {@code this} {@link Puzzle} is solved;
     *          {@code false} otherwise.
     */
    public boolean isSolved() {
        boolean solved = isLegal();

        for (Cell cell : this) {
            solved &= cell.isSolved();

            if (! solved) {
                break;
            }
        }

        return solved;
    }

    /**
     * Method to apply the argument {@link Rule} to solve {@code this}
     * {@link Puzzle}.
     *
     * @param   rule            The {@link Rule} to apply.
     *
     * @return  {@code true} if {@link Puzzle} is modified;
     *          {@code false} otherwise.
     */
    public boolean apply(Rule rule) { return rule.applyTo(this); }
}
