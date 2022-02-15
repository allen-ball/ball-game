package ball.game.ant.taskdefs;
/*-
 * ##########################################################################
 * Game Applications and Utilities
 * %%
 * Copyright (C) 2010 - 2022 Allen D. Ball
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
import ball.game.sudoku.Cell;
import ball.game.sudoku.Puzzle;
import ball.game.sudoku.Rule;
import ball.game.sudoku.RuleOfElimination;
import ball.util.ant.taskdefs.AnnotatedAntTask;
import ball.util.ant.taskdefs.AntTask;
import ball.util.ant.taskdefs.ClasspathDelegateAntTask;
import ball.util.ant.taskdefs.ConfigurableAntTask;
import java.util.ServiceLoader;
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
 * {@link.uri http://ant.apache.org/ Ant} {@link Task} to solve Sudoku.
 *
 * {@ant.task}
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 */
@AntTask("sudoku")
@NoArgsConstructor @ToString
public class SudokuTask extends Task implements AnnotatedAntTask, ClasspathDelegateAntTask, ConfigurableAntTask {
    @Getter @Setter @Accessors(chain = true, fluent = true)
    private ClasspathUtils.Delegate delegate = null;
    private final Puzzle puzzle = new Puzzle();

    public Puzzle getPuzzle() { return puzzle; }
    public void setPuzzle(String string) { parse(string); }

    public void addText(String text) { parse(text); }

    private void parse(String string) {
        string = string.replaceAll("[\\p{Space}]+", EMPTY);
        string = string.replaceAll("[^1-9]", ".");

        int i = 0;

        for (Cell cell : puzzle.values()) {
            if (i < string.length()) {
                if (string.charAt(i) != '.') {
                    cell.retainAll((int) string.charAt(i) - '0');
                }

                i += 1;
            } else {
                break;
            }
        }
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

        var loader = ServiceLoader.load(Rule.class, getClassLoader());
        var puzzle = getPuzzle();

        try {
            log(puzzle);

            if (! puzzle.isSolved()) {
                apply(new RuleOfElimination(), puzzle);
            }

            while (! puzzle.isSolved()) {
                var  modified = false;

                for (var rule : loader) {
                    modified |= apply(rule, puzzle);

                    if (puzzle.isSolved()) {
                        break;
                    }
                }

                if (! modified) {
                    throw new BuildException("Unsolved puzzle");
                }
            }
        } catch (BuildException exception) {
            throw exception;
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            throw new BuildException(throwable);
        }
    }

    private boolean apply(Rule rule, Puzzle puzzle) throws Exception {
        if (! puzzle.isLegal()) {
            throw new BuildException("Illegal puzzle");
        }

        var modified = rule.applyTo(puzzle);

        if (modified) {
            log();
            log(rule.getClass().getCanonicalName());
            log(puzzle);
        }

        return modified;
    }
}
