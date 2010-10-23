/*
 * $Id: Game.java,v 1.2 2010-10-23 22:22:04 ball Exp $
 *
 * Copyright 2010 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.life;

import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Life {@link Game}.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision: 1.2 $
 */
public class Game extends ArrayList<BigInteger> {
    private static final long serialVersionUID = -1658778825256180346L;

    private final Automata automata;

    /**
     * @param   height          The extent of the y-axis.
     * @param   width           The extent of the x-axis.
     * @param   state0          The initial state.
     */
    public Game(int height, int width, BigInteger state0) {
        super();

        automata = new Automata(height, width);

        add(state0);
    }

    /**
     * Method to get the {@link Automata} for this {@link Game}.
     *
     * @return  The {@link Automata}.
     */
    public Automata automata() { return automata; }

    /**
     * Method to get the {@link Board} representing the current state of
     * this {@link Game}.
     *
     * @return  The {@link Board}.
     */
    public Board board() { return new Board(automata(), current()); }

    /**
     * Method to get the current state of this {@link Game}.
     *
     * @return  The current {@code state}.
     */
    public BigInteger current() { return get(size() - 1); }
}
/*
 * $Log: not supported by cvs2svn $
 */
