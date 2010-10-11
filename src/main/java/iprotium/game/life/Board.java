/*
 * $Id: Board.java,v 1.1 2010-10-11 15:11:11 ball Exp $
 *
 * Copyright 2010 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.life;

import iprotium.text.TableModel;
import java.math.BigInteger;

/**
 * Life {@link Board}.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision: 1.1 $
 */
public class Board extends TableModel {
    private static final long serialVersionUID = -6927626665409704189L;

    private final Automata automata;
    private final BigInteger state;

    /**
     * @param   automata        The {@link Automata}.
     */
    public Board(Automata automata, BigInteger state) {
        super(automata.getWidth());

        this.automata = automata;
        this.state = state;
    }

    @Override
    public int getRowCount() { return automata.getHeight(); }

    @Override
    public String getValueAt(int y, int x) {
        return automata.get(state, y, x) ? "+" : "-";
    }
}
/*
 * $Log: not supported by cvs2svn $
 */
