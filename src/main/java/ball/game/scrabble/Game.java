/*
 * $Id$
 *
 * Copyright 2014 Allen D. Ball.  All rights reserved.
 */
package ball.game.scrabble;

import java.util.Collections;
import java.util.SortedSet;

/**
 * Scrabble {@link Game}.
 *
 * {@bean-info}
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class Game {
    private SortedSet<CharSequence> wordList =
        Collections.unmodifiableSortedSet(new OWL());
    private Board board = new Board();
    private Bag bag = new Bag();

    /**
     * Sole public constructor.
     */
    public Game() { }

    public SortedSet<CharSequence> getWordList() { return wordList; }
    public Board getBoard() { return board; }
    public Bag getBag() { return bag; }

    @Override
    public String toString() { return board.toString(); }
}
