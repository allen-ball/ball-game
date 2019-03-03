/*
 * $Id$
 *
 * Copyright 2014 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

import java.util.Collections;
import java.util.SortedSet;
import lombok.NoArgsConstructor;

/**
 * Scrabble {@link Game}.
 *
 * {@bean.info}
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
@NoArgsConstructor
public class Game {
    private SortedSet<CharSequence> wordList =
        Collections.unmodifiableSortedSet(new OWL());
    private Board board = new Board();
    private Bag bag = new Bag();

    public SortedSet<CharSequence> getWordList() { return wordList; }
    public Board getBoard() { return board; }
    public Bag getBag() { return bag; }

    @Override
    public String toString() { return board.toString(); }
}
