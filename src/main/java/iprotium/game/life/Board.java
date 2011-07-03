/*
 * $Id$
 *
 * Copyright 2010, 2011 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.life;

import iprotium.text.TableModel;

/**
 * Life {@link Board}.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision$
 */
public class Board extends TableModel {
    private static final long serialVersionUID = 1772285347250855101L;

    private final Game game;

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
