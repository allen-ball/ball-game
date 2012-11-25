/*
 * $Id: SudokuTask.java 681 2011-07-03 06:45:19Z ball $
 *
 * Copyright 2012 Allen D. Ball.  All rights reserved.
 */
package iprotium.game.ant.taskdefs;

import iprotium.game.sudoku.Cell;
import iprotium.game.sudoku.Puzzle;
import iprotium.game.sudoku.Rule;
import iprotium.text.TextTable;
import iprotium.util.ant.taskdefs.AbstractClasspathTask;
import java.util.ServiceLoader;
import org.apache.tools.ant.BuildException;

/**
 * <a href="http://ant.apache.org/">Ant</a> {@link Task} to solve Sudoku.
 *
 * @author <a href="mailto:ball@iprotium.com">Allen D. Ball</a>
 * @version $Revision: 681 $
 */
public class SudokuTask extends AbstractClasspathTask {
    private final Puzzle puzzle = new Puzzle();

    /**
     * Sole constructor.
     */
    public SudokuTask() { super(); }

    protected Puzzle getPuzzle() { return puzzle; }
    public void setPuzzle(String string) { parse(string); }

    public void addText(String text) { parse(text); }

    private void parse(String string) {
        string = string.replaceAll("[\\p{Space}]+", "");
        string = string.replaceAll("[^1-9]", ".");

        int i = 0;

        for (Cell cell : puzzle) {
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

                        log("");
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
        } catch (RuntimeException exception) {
            exception.printStackTrace();
            throw exception;
        } catch (Exception exception) {
            exception.printStackTrace();
            throw new BuildException(exception);
        }
    }

    private void log(Puzzle puzzle) {
        for (String line : new TextTable(puzzle)) {
            log(line);
        }
    }
}