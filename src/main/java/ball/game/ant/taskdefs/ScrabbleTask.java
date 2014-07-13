/*
 * $Id$
 *
 * Copyright 2014 Allen D. Ball.  All rights reserved.
 */
package ball.game.ant.taskdefs;

import ball.annotation.AntTask;
import ball.game.scrabble.Bag;
import ball.game.scrabble.Board;
import ball.game.scrabble.OWL;
import ball.game.scrabble.Rack;
import ball.game.scrabble.Tile;
import ball.text.TextTable;
import ball.util.ant.taskdefs.AbstractClasspathTask;
import java.util.Collections;
import java.util.List;
import java.util.LinkedHashSet;
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
                OWL owl = new OWL();
                Bag bag = new Bag();
                Rack rack = new Rack();

                for (char letter : getRack().toUpperCase().toCharArray()) {
                    rack.add(bag.draw(letter));
                }

                log(String.valueOf(rack));

                LinkedHashSet<String> set = new LinkedHashSet<String>();

                for (List<Tile> list : rack.combinations()) {
                    set.add(Tile.toString(list));
                }

                set.retainAll(owl);

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
