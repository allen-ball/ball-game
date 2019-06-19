/*
 * $Id$
 *
 * Copyright 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.crossword;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;
import static java.lang.Character.isLowerCase;
import static java.lang.Character.toLowerCase;
import static java.lang.Character.toUpperCase;

/**
 * Crossword {@link Puzzle} {@link Cell}.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class Cell {
    private static final Block BLOCK = new Block();

    private final Boolean special;
    private Character solution = null;

    private Cell(boolean special) { this.special = special; }

    private Cell(boolean special, Character solution) {
        this(special);

        setSolution(solution);
    }

    public boolean isBlock() { return false; }

    public boolean isSpecial() { return special; }

    public boolean isSolved() { return solution != null; }

    public Character getSolution() { return solution; }
    public void setSolution(Character solution) { this.solution = solution; }

    @Override
    public String toString() {
        char representation = '.';

        if (isSolved()) {
            representation = getSolution();

            if (isSpecial()) {
                representation = toLowerCase(representation);
            } else {
                representation = toUpperCase(representation);
            }
        }

        return String.valueOf(representation);
    }

    /**
     * Factory method to produce a {@link Cell}.
     *
     * @param   character       The {@code char} representing the
     *                          {@link Cell}.
     *
     * @return  The new {@link Cell}.
     */
    public static Cell getInstance(char character) {
        Cell cell = null;

        switch (character) {
        case '.':
            cell = new Cell(false);
            break;

        case '#':
            cell = BLOCK;
            break;

        default:
            if (isLetter(character)) {
                cell =
                    new Cell(isLowerCase(character), toUpperCase(character));
            } else if (isDigit(character)) {
                cell = new Cell(false, character);
            } else {
                throw new IllegalArgumentException(String.valueOf(character));
            }
            break;
        }

        return cell;
    }

    public static List<Cell> getRowFrom(String line) {
        return (line.chars()
                .mapToObj(c -> getInstance((char) c))
                .collect(Collectors.toList()));
    }

    private static class Block extends Cell {
        private Block() { super(false); }

        @Override
        public boolean isBlock() { return true; }

        @Override
        public boolean isSolved() { return true; }

        @Override
        public void setSolution(Character solution) {
            throw new IllegalStateException();
        }

        @Override
        public String toString() { return "#"; }
    }
}
