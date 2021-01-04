package ball.game.scrabble;
/*-
 * ##########################################################################
 * Game Applications and Utilities
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2010 - 2021 Allen D. Ball
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ##########################################################################
 */
import static java.util.Objects.requireNonNull;

/**
 * Scrabble {@link Board} square.
 *
 * {@bean.info}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
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
