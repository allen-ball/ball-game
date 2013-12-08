/*
 * $Id$
 *
 * Copyright 2011 - 2013 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.scrabble;

import java.util.ResourceBundle;

/**
 * Scrabble {@link Board}.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class Board {
    private static final int HEIGHT = 15;
    private static final int WIDTH = 15;

    private final Square[][] board = new Square[HEIGHT][];

    {
        for (int y = 0; y < board.length; y += 1) {
            board[y] = new Square[WIDTH];
        }
    }

    /**
     * Sole constructor.
     */
    public Board() {
        ResourceBundle bundle = ResourceBundle.getBundle(getClass().getName());

        reflect(0, 0, new TW());
        reflect(0, 3, new DL());
        reflect(0, 7, new TW());
        reflect(1, 1, new DW());
        reflect(1, 5, new TL());
        reflect(2, 2, new DW());
        reflect(3, 0, new DL());
        reflect(3, 3, new DW());
        reflect(3, 7, new DL());
        reflect(4, 4, new DW());
        reflect(5, 1, new TL());
        reflect(5, 5, new TL());
        reflect(6, 2, new DL());
        reflect(6, 6, new DL());
        reflect(7, 0, new TW());
        reflect(7, 7, new DW());

        for (int y = 0; y < board.length; y += 1) {
            for (int x = 0; x < board[y].length; x += 1) {
                if (board[y][x] == null) {
                    board[y][x] = new Square();
                }
            }
        }
    }

    private void reflect(int y, int x, Square square) {
        board[y][x] = square;

        int ym = (HEIGHT - 1) - y;
        int xm = (WIDTH - 1) - x;

        try {
            board[ym][x] = square.clone();
            board[y][xm] = square.clone();
            board[ym][xm] = square.clone();
        } catch (CloneNotSupportedException exception) {
	    throw new InternalError();
        }
    }

    @Override
    public String toString() { return super.toString(); }
}
