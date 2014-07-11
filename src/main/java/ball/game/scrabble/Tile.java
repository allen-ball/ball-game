/*
 * $Id$
 *
 * Copyright 2014 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

import java.beans.ConstructorProperties;

/**
 * Scrabble {@link Tile}.
 *
 * {@bean-info}
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class Tile implements Cloneable {
    private static final char BLANK = '_';

    private final char letter;
    private final int points;

    /**
     * Sole constructor.
     *
     * @param   letter          The {@link Tile} letter value.
     * @param   points          The {@link Tile} points value.
     */
    @ConstructorProperties({ "letter", "points" })
    public Tile(char letter, int points) {
        this.letter = letter;
        this.points = points;
    }

    public char getLetter() { return letter; }
    public int getPoints() { return points; }
    public boolean isBlank() { return (getLetter() == BLANK); }

    @Override
    public Tile clone() throws CloneNotSupportedException {
        return (Tile) super.clone();
    }

    @Override
    public String toString() { return String.valueOf(letter); }
}
