/*
 * $Id$
 *
 * Copyright 2011 - 2018 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

import static java.util.Objects.requireNonNull;

/**
 * Scrabble {@link Board} square.
 *
 * {@bean.info}
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class SQ {
    private final int letterPremium;
    private final int wordPremium;
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
    protected SQ(String string) {
        if (getClass().getAnnotation(LetterPremium.class) != null) {
            letterPremium =
                getClass().getAnnotation(LetterPremium.class).value();
        } else {
            letterPremium = 0;
        }

        if (getClass().getAnnotation(WordPremium.class) != null) {
            wordPremium = getClass().getAnnotation(WordPremium.class).value();
        } else {
            wordPremium = 0;
        }

        this.string = requireNonNull(string, "string");
    }

    public boolean isPremium() {
        return (letterPremium > 1 || wordPremium > 1);
    }

    /**
     * @return  The letter premium value.
     *
     * @see LetterPremium
     */
    public int getLetterPremium() { return letterPremium; }

    /**
     * @return  The word premium value.
     *
     * @see WordPremium
     */
    public int getWordPremium() { return wordPremium; }

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
