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
import ball.activation.ReaderWriterDataSource;
import ball.game.crossword.Puzzle;
import ball.util.ant.taskdefs.AnnotatedAntTask;
import ball.util.ant.taskdefs.AntTask;
import ball.util.ant.taskdefs.ClasspathDelegateAntTask;
import ball.util.ant.taskdefs.ConfigurableAntTask;
import ball.util.ant.taskdefs.NotNull;
import java.io.BufferedReader;
import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.stream.Stream;
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
 * Abstract XD {@link.uri http://ant.apache.org/ Ant} {@link Task}
 * base class.
 *
 * {@bean.info}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
@NoArgsConstructor(access = PROTECTED)
public abstract class XDTask extends Task implements AnnotatedAntTask,
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
     * {@link.uri http://ant.apache.org/ Ant} {@link Task} to load a
     * {@link Puzzle} from an
     * {@link.uri https://github.com/century-arcade/xd target=newtab XD}
     * {@link File}.
     *
     * {@bean.info}
     */
    @AntTask("xd-load")
    @NoArgsConstructor @ToString
    public static class Load extends XDTask {
        @NotNull @Getter @Setter
        private File file = null;
        protected Puzzle puzzle = null;

        @Override
        public void execute() throws BuildException {
            super.execute();

            try {
                puzzle = Puzzle.load(getFile().getAbsolutePath());

                var ds = new ReaderWriterDataSource(null, null);

                puzzle.writeTo(ds.getPrintWriter());

                try (var reader = ds.getBufferedReader()) {
                    log(reader.lines());
                }
            } catch (BuildException exception) {
                throw exception;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                throw new BuildException(throwable);
            }
        }
    }

    /**
     * {@link.uri http://ant.apache.org/ Ant} {@link Task} to load a
     * {@link Puzzle} from an
     * {@link.uri https://github.com/century-arcade/xd target=newtab XD}
     * {@link File} and return a {@link Stream} of possible solutions.
     *
     * {@bean.info}
     */
    @AntTask("xd-solve")
    @NoArgsConstructor @ToString
    public static class Solve extends Load {
        @Override
        public void execute() throws BuildException {
            super.execute();

            try {
                Stream<Puzzle> stream = puzzle.solve(getWordList());
                Iterator<Puzzle> iterator = stream.iterator();

                while (iterator.hasNext()) {
                    log();

                    Puzzle solution = iterator.next();

                    /* log(solution.isSolved() ? "COMPLETE" : "PARTIAL"); */
                    log(solution);
                }
            } catch (BuildException exception) {
                throw exception;
            } catch (Throwable throwable) {
                throwable.printStackTrace();
                throw new BuildException(throwable);
            }
        }

        private Set<CharSequence> getWordList() {
            return new ball.game.scrabble.wordlist.TWL06().keySet();
        }
    }
}
