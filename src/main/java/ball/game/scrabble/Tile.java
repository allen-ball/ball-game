/*
 * $Id$
 *
 * Copyright 2014 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

import java.beans.ConstructorProperties;
import java.util.Collection;

import static java.util.stream.Collectors.joining;

/**
 * Scrabble {@link Tile}.
 *
 * {@bean.info}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public class Tile {

    /**
     * {@link #BLANK} = {@value #BLANK}
     */
    public static final char BLANK = '_';

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
    public String toString() { return String.valueOf(letter); }

    /**
     * Static method to return the {@link String} represented by the
     * {@link Collection} of {@link Tile}s.
     *
     * @param   collection      The {@link Collection} of {@link Tile}s.
     *
     * @return  The {@link String} representation.
     */
    public static String toString(Collection<Tile> collection) {
        return collection.stream().map(t -> t.toString()).collect(joining());
    }
}
