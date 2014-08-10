/*
 * $Id$
 *
 * Copyright 2010 - 2014 Allen D. Ball.  All rights reserved.
 */
package ball.game.ant.taskdefs;

import ball.game.life.Board;
import ball.game.life.Game;
import ball.util.ant.taskdefs.AbstractClasspathTask;
import ball.util.ant.taskdefs.AntTask;
import java.math.BigInteger;
import org.apache.tools.ant.BuildException;

import static ball.util.StringUtil.NIL;

/**
 * {@link.uri http://ant.apache.org/ Ant} {@link org.apache.tools.ant.Task}
 * to start {@link Game} of Life simulation.
 *
 * {@bean-info}
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
@AntTask("life")
public class LifeTask extends AbstractClasspathTask {
    private int height = 0;
    private int width = 0;
    private BigInteger state0 = BigInteger.ZERO;

    /**
     * Sole constructor.
     */
    public LifeTask() { super(); }

    public int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    public int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    public BigInteger getState0() { return state0; }
    public void setState0(String state0) { this.state0 = parse(state0); }

    public void addText(String text) { setState0(text); }

    private BigInteger parse(String string) {
        BigInteger state = null;

        try {
            state = new BigInteger(string);
        } catch (NumberFormatException exception) {
            state = BigInteger.ZERO;
            string = string.replaceAll("[\\p{Space}]+", NIL);

            for (int i = 0, n = string.length(); i < n; i += 1) {
                switch (string.charAt(i)) {
                case '+':
                    state = state.setBit(i);
                    break;

                case '-':
                default:
                    state = state.clearBit(i);
                    break;
                }
            }
        }

        return state;
    }

    @Override
    public void execute() throws BuildException {
        super.execute();

        try {
            Game game = new Game(getHeight(), getWidth(), getState0());
            Board board = new Board(game);

            for (;;) {
                log(NIL);
                log("Generation #" + String.valueOf(game.size() - 1));
                log(board);

                BigInteger state = game.automata().next(game.getLast());

                if (! game.contains(state)) {
                    game.addLast(state);
                    continue;
                } else {
                    log("Steady state: Returned to Generation #"
                        + String.valueOf(game.indexOf(state)));
                    break;
                }
            }
        } catch (BuildException exception) {
            throw exception;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new BuildException(throwable);
        }
    }
}
