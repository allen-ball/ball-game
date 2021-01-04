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
