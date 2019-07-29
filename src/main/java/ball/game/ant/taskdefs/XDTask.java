/*
 * $Id$
 *
 * Copyright 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.ant.taskdefs;

import ball.activation.ReaderWriterDataSource;
import ball.game.crossword.Puzzle;
import ball.util.ant.taskdefs.AnnotatedAntTask;
import ball.util.ant.taskdefs.AntTask;
import ball.util.ant.taskdefs.ClasspathDelegateAntTask;
import ball.util.ant.taskdefs.ConfigurableAntTask;
import ball.util.ant.taskdefs.NotNull;
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
import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * Abstract XD {@link.uri http://ant.apache.org/ Ant} {@link Task}
 * base class.
 *
 * {@bean.info}
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
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

                ReaderWriterDataSource ds =
                    new ReaderWriterDataSource(null, null);

                puzzle.writeTo(ds.getPrintWriter());

                log(ds);
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
                    log(EMPTY);

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
            return new ball.game.scrabble.wordlist.TWL06();
        }
    }
}
