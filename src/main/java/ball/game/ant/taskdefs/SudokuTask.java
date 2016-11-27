/*
 * $Id$
 *
 * Copyright 2012 - 2016 Allen D. Ball.  All rights reserved.
 */
package ball.game.ant.taskdefs;

import ball.game.sudoku.Cell;
import ball.game.sudoku.Puzzle;
import ball.game.sudoku.Rule;
import ball.util.ant.taskdefs.AbstractClasspathTask;
import ball.util.ant.taskdefs.AntTask;
import java.util.ServiceLoader;
import org.apache.tools.ant.BuildException;

import static ball.util.StringUtil.NIL;

/**
 * {@link.uri http://ant.apache.org/ Ant} {@link org.apache.tools.ant.Task}
 * to solve Sudoku.
 *
 * {@bean.info}
 *
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
@AntTask("sudoku")
public class SudokuTask extends AbstractClasspathTask {
    private final Puzzle puzzle = new Puzzle();

    /**
     * Sole constructor.
     */
    public SudokuTask() { super(); }

    public Puzzle getPuzzle() { return puzzle; }
    public void setPuzzle(String string) { parse(string); }

    public void addText(String text) { parse(text); }

    private void parse(String string) {
        string = string.replaceAll("[\\p{Space}]+", NIL);
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
    public void execute() throws BuildException {
        super.execute();

        ServiceLoader<Rule> loader =
            ServiceLoader.load(Rule.class, getClassLoader());
        Puzzle puzzle = getPuzzle();

        try {
            log(puzzle);

            while (! puzzle.isSolved()) {
                if (! puzzle.isLegal()) {
                    throw new BuildException("Illegal puzzle");
                }

                boolean modified = false;

                for (Rule rule : loader) {
                    if (rule.applyTo(puzzle)) {
                        modified |= true;

                        log(NIL);
                        log(rule.getClass().getCanonicalName());
                        log(puzzle);

                        if (puzzle.isSolved()) {
                            break;
                        }
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
}
