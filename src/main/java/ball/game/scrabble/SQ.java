/*
 * $Id$
 *
 * Copyright 2011 - 2014 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

/**
 * Scrabble {@link Board} square.
 *
 * {@bean-info}
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class SQ {
    private Tile tile = null;
    private char letter = ' ';
    private String string = null;

    /**
     * Sole public constructor.
     */
    public SQ() { this("."); }

    /**
     * Protected constructor to specify the {@link SQ} representation.
     *
     * @param   string          The {@link String} representation of this
     *                          {@link SQ} if no {@link Tile} has been
     *                          played.
     */
    protected SQ(String string) { this.string = string; }

    public boolean isEmpty() { return (tile == null); }

    public void play(Tile tile) { play(tile, tile.getLetter()); }

    public void play(Tile tile, char letter) {
        if (isEmpty()) {
            this.tile = tile;
            this.letter = letter;
        } else {
            throw new IllegalStateException();
        }

        if (tile.isBlank()) {
            string = String.valueOf(letter).toLowerCase();
        } else {
            string = tile.toString().toUpperCase();
        }
    }

    @Override
    public String toString() { return string; }
}
