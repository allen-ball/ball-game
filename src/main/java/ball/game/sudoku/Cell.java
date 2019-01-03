/*
 * $Id$
 *
 * Copyright 2012 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.sudoku;

/**
 * Sudoku {@link Cell}.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class Cell extends Digits {
    private static final long serialVersionUID = -2029175165124035385L;

    /**
     * {@link #UNKNOWN} = {@value #UNKNOWN}
     */
    public static final String UNKNOWN = ".";

    /**
     * Sole constructor.  Construct with all possible digits.
     */
    public Cell() {
        super();

        addAll(ALL);
    }

    /**
     * Method to determine if {@link.this} {@link Cell} is solved.
     *
     * @return  {@code true} if the {@link Cell} is solved; {@code false}
     *          otherwise.
     */
    public boolean isSolved() { return (size() == 1); }

    /**
     * Method to get {@link.this} {@link Cell}'s solution.
     *
     * @return  The solution if the {@link Cell} is solved; {@code null}
     *          otherwise.
     */
    public Integer solution() { return isSolved() ? first() : null; }

    /**
     * @return  The minimum value of the {@link Cell}.
     *
     * @see #first()
     */
    public Integer min() { return first(); }

    /**
     * @return  The maximum value of the {@link Cell}.
     *
     * @see #last()
     */
    public Integer max() { return last(); }

    /**
     * Method to determine if {@link.this} {@link Cell} is in the specified
     * {@link Iterable} with {@code ==}.
     *
     * @param   iterable        The {@link Iterable} of {@link Object}s to
     *                          test.
     *
     * @return  {@code true} if the {@link Cell} is in the {@link Iterable};
     *          {@code false} otherwise.
     */
    public boolean isIn(Iterable<?> iterable) {
        boolean isContained = false;

        for (Object object : iterable) {
            isContained |= (this == object);

            if (isContained) {
                break;
            }
        }

        return isContained;
    }

    @Override
    public String toString() {
        return isSolved() ? String.valueOf(first()) : toString(this);
    }

    private String toString(Cell cell) {
        StringBuilder buffer = new StringBuilder();

        if (cell.size() == ALL.size()) {
            buffer.append(".");
        } else {
            buffer.append("[");

            for (int digit : cell) {
                buffer.append(digit);
            }

            buffer.append("]");
        }

        return buffer.toString();
    }
}
