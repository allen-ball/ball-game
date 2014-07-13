/*
 * $Id$
 *
 * Copyright 2014 Allen D. Ball.  All rights reserved.
 */
package ball.game.ant.taskdefs;

import ball.annotation.AntTask;
import ball.game.scrabble.AI;
import ball.game.scrabble.Board;
import ball.game.scrabble.Game;
import ball.game.scrabble.Player;
import ball.game.scrabble.Rack;
import ball.game.scrabble.Tile;
import ball.text.TextTable;
import ball.util.ant.taskdefs.AbstractClasspathTask;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import org.apache.tools.ant.BuildException;

/**
 * Abstract Scrabble {@link.uri http://ant.apache.org/ Ant}
 * {@link org.apache.tools.ant.Task} base class.
 *
 * {@bean-info}
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public abstract class ScrabbleTask extends AbstractClasspathTask {

    /**
     * Sole constructor.
     */
    protected ScrabbleTask() { super(); }

    protected void log(Board board) {
        for (String line : new TextTable(board)) {
            log(line);
        }
    }

    /**
     * {@link.uri http://ant.apache.org/ Ant}
     * {@link org.apache.tools.ant.Task} to find possible words for a
     * {@link Rack}.
     *
     * {@bean-info}
     */
    @AntTask("scrabble-words-for")
    public static class WordsFor extends ScrabbleTask {
        private String rack = null;

        /**
         * Sole constructor.
         */
        public WordsFor() { super(); }

        public void setRack(String rack) { this.rack = rack; }
        protected String getRack() { return rack; }

        @Override
        public void execute() throws BuildException {
            if (getRack() == null) {
                throw new BuildException("`rack' attribute must be specified");
            }

            try {
                Game game = new Game();
                Player player = new AI();

                for (char letter : getRack().toUpperCase().toCharArray()) {
                    player.getRack().add(game.getBag().draw(letter));
                }

                log(String.valueOf(player.getRack()));

                LinkedHashSet<String> set = new LinkedHashSet<String>();

                for (List<Tile> list : player.getRack().combinations()) {
                    set.add(Tile.toString(list));
                }

                set.retainAll(game.getWordList());

                log(String.valueOf(set));
            } catch (BuildException exception) {
                throw exception;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                throw new BuildException(throwable);
            }
        }
    }
}
