/*
 * $Id$
 *
 * Copyright 2014 - 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.ant.taskdefs;

import ball.game.scrabble.AI;
import ball.game.scrabble.Board;
import ball.game.scrabble.Game;
import ball.game.scrabble.Player;
import ball.game.scrabble.Rack;
import ball.game.scrabble.Tile;
import ball.util.ant.taskdefs.AnnotatedAntTask;
import ball.util.ant.taskdefs.AntTask;
import ball.util.ant.taskdefs.ClasspathDelegateAntTask;
import ball.util.ant.taskdefs.ConfigurableAntTask;
import ball.util.ant.taskdefs.NotNull;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.util.ClasspathUtils;

import static lombok.AccessLevel.PROTECTED;

/**
 * Abstract Scrabble {@link.uri http://ant.apache.org/ Ant} {@link Task}
 * base class.
 *
 * {@bean.info}
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
@NoArgsConstructor(access = PROTECTED)
public abstract class ScrabbleTask extends Task
                                   implements AnnotatedAntTask,
                                              ClasspathDelegateAntTask,
                                              ConfigurableAntTask  {
    @Getter @Setter @Accessors(chain = true, fluent = true)
    private ClasspathUtils.Delegate delegate = null;

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
    }

    /**
     * {@link.uri http://ant.apache.org/ Ant}
     * {@link org.apache.tools.ant.Task} to find possible words for a
     * {@link Rack}.
     *
     * {@bean.info}
     */
    @AntTask("scrabble-words-for")
    @NoArgsConstructor @ToString
    public static class WordsFor extends ScrabbleTask {
        @NotNull @Getter @Setter
        private String rack = null;

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
