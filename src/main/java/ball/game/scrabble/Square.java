/*
 * $Id$
 *
 * Copyright 2011 - 2014 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

/**
 * Scrabble {@link Board} {@link Square}.
 *
 * {@bean-info}
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class Square implements Cloneable {
    private Tile tile = null;
    private char letter = ' ';
    private String string = null;

    /**
     * Sole public constructor.
     */
    public Square() { this("."); }

    /**
     * Protected constructor to specify the {@link Square} representation.
     *
     * @param   string          The {@link String} representation of this
     *                          {@link Square} if no {@link Tile} has been
     *                          played.
     */
    protected Square(String string) { this.string = string; }

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
            string = "[" + letter + "]";
        } else {
            string = tile.toString();
        }
    }

    @Override
    public String toString() { return string; }
}
