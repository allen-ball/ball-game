/*
 * $Id$
 *
 * Copyright 2014 Allen D. Ball.  All rights reserved.
 */
package ball.game.ant.taskdefs;

import ball.annotation.AntTask;
import ball.game.scrabble.Bag;
import ball.game.scrabble.Board;
import ball.text.TextTable;
import ball.util.ant.taskdefs.AbstractClasspathTask;
import java.util.Collections;
import org.apache.tools.ant.BuildException;

/**
 * {@link.uri http://ant.apache.org/ Ant} {@link org.apache.tools.ant.Task}
 * to play Scrabble.
 *
 * {@bean-info}
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
@AntTask("scrabble")
public class ScrabbleTask extends AbstractClasspathTask {

    /**
     * Sole constructor.
     */
    public ScrabbleTask() { super(); }

    @Override
    public void execute() throws BuildException {
        try {
            Board board = new Board();
            Bag bag = new Bag();

            Collections.shuffle(bag);

            log(board);
            log(String.valueOf(bag));
        } catch (BuildException exception) {
            throw exception;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new BuildException(throwable);
        }
    }

    private void log(Board board) {
        for (String line : new TextTable(board)) {
            log(line);
        }
    }
}
