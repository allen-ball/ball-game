/*
 * $Id: LifeTask.java,v 1.1 2010-10-11 15:12:02 ball Exp $
 *
 * Copyright 2010 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.ant.taskdefs;

import iprotium.game.life.Game;
import iprotium.text.Table;
import java.math.BigInteger;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;

/**
 * <a href="http://ant.apache.org/">Ant</a> {@link Task} to start
 * {@link Game} of Life simulation.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision: 1.1 $
 */
public class LifeTask extends Task {
    private int height = 0;
    private int width = 0;
    private BigInteger state0 = BigInteger.ZERO;

    /**
     * Sole constructor.
     */
    public LifeTask() { super(); }

    protected int getHeight() { return height; }
    public void setHeight(int height) { this.height = height; }

    protected int getWidth() { return width; }
    public void setWidth(int width) { this.width = width; }

    protected BigInteger getState0() { return state0; }
    public void setState0(String state0) { this.state0 = parse(state0); }

    public void addText(String text) { setState0(text); }

    private BigInteger parse(String string) {
        BigInteger state = null;

        try {
            state = new BigInteger(string);
        } catch (NumberFormatException exception) {
            state = BigInteger.ZERO;
            string = string.replaceAll("[\\p{Space}]+", "");

            for (int i = 0, n = string.length(); i < n; i += 1) {
                switch (string.charAt(i)) {
                case '+':
                    state = state.setBit(i);
                    break;

                default:
                    break;
                }
            }
        }

        return state;
    }

    @Override
    public void execute() throws BuildException {
        try {
            Game game = new Game(getHeight(), getWidth(), getState0());

            for (;;) {
                log("");
                log("Generation #" + String.valueOf(game.size() - 1));

                for (String line : new Table(game.board())) {
                    log(line);
                }

                BigInteger state = game.automata().next(game.current());

                if (! game.contains(state)) {
                    game.add(state);
                } else {
                    log("Steady state: Returned to Generation #"
                        + String.valueOf(game.indexOf(state)));
                    break;
                }
            }
        } catch (BuildException exception) {
            throw exception;
        } catch (RuntimeException exception) {
            exception.printStackTrace();
            throw exception;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BuildException(exception);
        }
    }
}
/*
 * $Log: not supported by cvs2svn $
 */
