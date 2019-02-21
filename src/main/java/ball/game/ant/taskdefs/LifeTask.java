/*
 * $Id$
 *
 * Copyright 2010 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.ant.taskdefs;

import ball.game.life.Board;
import ball.game.life.Game;
import ball.util.ant.taskdefs.AnnotatedAntTask;
import ball.util.ant.taskdefs.AntTask;
import ball.util.ant.taskdefs.ClasspathDelegateAntTask;
import ball.util.ant.taskdefs.ConfigurableAntTask;
import java.math.BigInteger;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.ClasspathUtils;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * {@link.uri http://ant.apache.org/ Ant} {@link Task} to start {@link Game}
 * of Life simulation.
 *
 * {@ant.task}
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
@AntTask("life")
@NoArgsConstructor @ToString
public class LifeTask extends Task implements AnnotatedAntTask,
                                              ClasspathDelegateAntTask,
                                              ConfigurableAntTask {
    @Getter @Setter @Accessors(chain = true, fluent = true)
    private ClasspathUtils.Delegate delegate = null;
    @Getter @Setter
    private int height = 0;
    @Getter @Setter
    private int width = 0;
    @Getter
    private BigInteger state0 = BigInteger.ZERO;

    public void setState0(String state0) { this.state0 = parse(state0); }

    public void addText(String text) { setState0(text); }

    private BigInteger parse(String string) {
        BigInteger state = null;

        try {
            state = new BigInteger(string);
        } catch (NumberFormatException exception) {
            state = BigInteger.ZERO;
            string = string.replaceAll("[\\p{Space}]+", EMPTY);

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
    public void init() throws BuildException {
        super.init();
        ClasspathDelegateAntTask.super.init();
        ConfigurableAntTask.super.init();
    }

    @Override
    public void execute() throws BuildException {
        super.execute();
        AnnotatedAntTask.super.execute();

        try {
            Game game = new Game(getHeight(), getWidth(), getState0());
            Board board = new Board(game);

            for (;;) {
                log(EMPTY);
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
