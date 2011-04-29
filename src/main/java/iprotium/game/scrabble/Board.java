/*
 * $Id: Board.java,v 1.1 2011-04-29 02:43:19 ball Exp $
 *
 * Copyright 2011 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.scrabble;

/**
 * Scrabble {@link Board}.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision: 1.1 $
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

        board[ym][x] = square.clone();
        board[y][xm] = square.clone();
        board[ym][xm] = square.clone();
    }

    private class DL extends Square {
        public DL() { super(); }

        @Override
        public DL clone() { return (DL) super.clone(); }
    }

    private class DW extends Square {
        public DW() { super(); }

        @Override
        public DW clone() { return (DW) super.clone(); }
    }

    private class TL extends Square {
        public TL() { super(); }

        @Override
        public TL clone() { return (TL) super.clone(); }
    }

    private class TW extends Square {
        public TW() { super(); }

        @Override
        public TW clone() { return (TW) super.clone(); }
    }
}
/*
 * $Log: not supported by cvs2svn $
 */
