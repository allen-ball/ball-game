package ball.game.ant.taskdefs;
/*-
 * ##########################################################################
 * Game Applications and Utilities
 * $Id$
 * $HeadURL$
 * %%
 * Copyright (C) 2010 - 2021 Allen D. Ball
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

import static java.util.stream.Collectors.toCollection;
import static lombok.AccessLevel.PROTECTED;

/**
 * Abstract Scrabble {@link.uri http://ant.apache.org/ Ant} {@link Task}
 * base class.
 *
 * {@ant.task}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@NoArgsConstructor(access = PROTECTED)
public abstract class ScrabbleTask extends Task
                                   implements AnnotatedAntTask,
                                              ClasspathDelegateAntTask,
                                              ConfigurableAntTask {
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
     * {@link.uri http://ant.apache.org/ Ant} {@link Task} to find possible
     * words for a {@link Rack}.
     *
     * {@ant.task}
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
                var game = new Game();
                var player = new AI();

                for (var letter : getRack().toUpperCase().toCharArray()) {
                    player.getRack().add(game.getBag().draw(letter));
                }

                log(String.valueOf(player.getRack()));

                var set =
                    player.getRack().combinations()
                    .map(t -> Tile.toString(t))
                    .collect(toCollection(LinkedHashSet::new));

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
