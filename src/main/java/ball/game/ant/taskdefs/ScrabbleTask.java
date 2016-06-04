/*
 * $Id$
 *
 * Copyright 2014 - 2016 Allen D. Ball.  All rights reserved.
 */
package ball.game.ant.taskdefs;

import ball.game.scrabble.AI;
import ball.game.scrabble.Board;
import ball.game.scrabble.Game;
import ball.game.scrabble.Player;
import ball.game.scrabble.Rack;
import ball.game.scrabble.Tile;
import ball.util.ant.taskdefs.AbstractClasspathTask;
import ball.util.ant.taskdefs.AntTask;
import ball.util.ant.taskdefs.NotNull;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import org.apache.tools.ant.BuildException;

/**
 * Abstract Scrabble {@link.uri http://ant.apache.org/ Ant}
 * {@link org.apache.tools.ant.Task} base class.
 *
 * {@bean.info}
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public abstract class ScrabbleTask extends AbstractClasspathTask {

    /**
     * Sole constructor.
     */
    protected ScrabbleTask() { super(); }

    /**
     * {@link.uri http://ant.apache.org/ Ant}
     * {@link org.apache.tools.ant.Task} to find possible words for a
     * {@link Rack}.
     *
     * {@bean.info}
     */
    @AntTask("scrabble-words-for")
    public static class WordsFor extends ScrabbleTask {
        private String rack = null;

        /**
         * Sole constructor.
         */
        public WordsFor() { super(); }

        @NotNull
        public String getRack() { return rack; }
        public void setRack(String rack) { this.rack = rack; }

        @Override
        public void execute() throws BuildException {
            super.execute();

            try {
                Game game = new Game();
                Player player = new AI();

                for (char letter : getRack().toUpperCase().toCharArray()) {
                    player.getRack().add(game.getBag().draw(letter));
                }

                log(String.valueOf(player.getRack()));

                LinkedHashSet<String> set = new LinkedHashSet<>();

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
