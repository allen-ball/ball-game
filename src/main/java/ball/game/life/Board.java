/*
 * $Id$
 *
 * Copyright 2010 - 2018 Allen D. Ball.  All rights reserved.
 */
package ball.game.life;

import ball.swing.table.AbstractTableModelImpl;

/**
 * Life {@link Board}.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public class Board extends AbstractTableModelImpl {
    private static final long serialVersionUID = -8272117924997665864L;

    /** @serial */ private final Game game;

    /**
     * @param   game            The {@link Game}.
     */
    public Board(Game game) {
        super(game.automata().getWidth());

        this.game = game;
    }

    public Game game() { return game; }

    @Override
    public int getRowCount() { return game().automata().getHeight(); }

    @Override
    public String getValueAt(int y, int x) {
        Game game = game();

        return game.automata().get(game.getLast(), y, x) ? "+" : "-";
    }
}
