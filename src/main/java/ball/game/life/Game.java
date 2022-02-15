package ball.game.life;
/*-
 * ##########################################################################
 * Game Applications and Utilities
 * %%
 * Copyright (C) 2010 - 2022 Allen D. Ball
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
import java.math.BigInteger;
import java.util.LinkedList;

/**
 * Life {@link Game}.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
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
