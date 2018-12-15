/*
 * $Id$
 *
 * Copyright 2010 - 2018 Allen D. Ball.  All rights reserved.
 */
package ball.game.life;

import java.math.BigInteger;
import java.util.LinkedList;

/**
 * Life {@link Game}.
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class Game extends LinkedList<BigInteger> {
    private static final long serialVersionUID = 7650642647319679493L;

    /** @serial */ private final Automata automata;

    /**
     * @param   height          The extent of the y-axis.
     * @param   width           The extent of the x-axis.
     * @param   state0          The initial state.
     */
    public Game(int height, int width, BigInteger state0) {
        super();

        automata = new Automata(height, width);

        addLast(state0);
    }

    /**
     * Method to get the {@link Automata} for this {@link Game}.
     *
     * @return  The {@link Automata}.
     */
    public Automata automata() { return automata; }
}
