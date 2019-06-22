/*
 * $Id$
 *
 * Copyright 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.crossword;

import ball.util.Coordinate;
import ball.util.CoordinateMap;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;

/**
 * Crossword {@link Puzzle}.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public class Puzzle extends CoordinateMap<Cell> {
    private static final long serialVersionUID = 6997638707307424791L;

    private static final List<String> HEADERS =
        Arrays.asList("Title", "Author", "Editor", "Special", "Rebus", "Date");
    private static final List<String> BOUNDARY = Arrays.asList(EMPTY, EMPTY);

    private static final String COLON = ":";
    private static final String DOT = ".";
    private static final String TILDE = "~";

    /** @serial */
    private final LinkedHashMap<String,String> headers = new LinkedHashMap<>();
    /** @serial */
    private final List<Coordinate> indices;
    /** @serial */
    private final Map<Label,Solution> answers;
    /** @serial */
    private final TreeMap<Label,String> clues = new TreeMap<>();
    /** @serial */
    private final ArrayList<String> notes = new ArrayList<>();

    private Puzzle(List<String> headers, List<String> grid,
                   List<String> clues, List<String> notes) {
        super(Cell.class);

        for (String key : HEADERS) {
            this.headers.put(key, EMPTY);
        }

        for (String string : headers) {
            String[] strings = string.split(Pattern.quote(COLON), 2);

            this.headers.put(strings[0].trim(),
                             (strings.length > 0) ? strings[1].trim() : EMPTY);
        }

        this.headers.remove(EMPTY);
        this.headers.values().removeAll(Arrays.asList(EMPTY, null));

        for (String line : grid) {
            List<Cell> row = Cell.getRowFrom(line.trim());

            if ((getColumnCount() * getRowCount()) > 0) {
                if (getColumnCount() != row.size()) {
                    throw new IllegalArgumentException(line + " does not have "
                                                       + getColumnCount()
                                                       + " columns");
                }
            }

            resize(getRowCount() + 1, row.size());
            Collections.copy(row(getRowCount() - 1).asList(), row);
        }

        List<Solution> list = new ArrayList<>();
        List<CoordinateMap<Cell>> lines =
            new ArrayList<>(getRowCount() + getColumnCount());

        lines.addAll(rows());
        lines.addAll(columns());

        for (CoordinateMap<Cell> line : lines) {
            list.add(new Solution());

            for (Map.Entry<Coordinate,Cell> entry : line.entrySet()) {
                if (! entry.getValue().isBlock()) {
                    list.get(list.size() - 1)
                        .put(entry.getKey(), entry.getValue());
                } else {
                    list.add(new Solution());
                }
            }
        }

        list =
            list.stream()
            .filter(t -> t.size() > 1)
            .collect(Collectors.toList());

        this.indices =
            list.stream()
            .map(t -> t.getCoordinate())
            .collect(Collectors.toCollection(TreeSet::new))
            .stream()
            .collect(Collectors.toList());

        this.answers =
            list.stream()
            .collect(Collectors.toMap(k -> k.label(), v -> v));

        for (String string : clues) {
            if (StringUtils.isNotBlank(string)) {
                String[] substrings = string.split("[. ]+", 2);
                Label label = Label.parse(substrings[0]);

                substrings = substrings[1].split("[~]", 2);

                this.clues.put(label, substrings[0].trim());

                if (substrings.length > 1) {
                    String solution = substrings[1];
                    /*
                     * To-Do: Verify solution.
                     */
                }
            }
        }

        this.notes.addAll(notes);

        this.answers.keySet()
            .stream()
            .forEach(t -> this.clues.computeIfAbsent(t, k -> "TBD"));
    }

    public Map<String,String> headers() {
        return Collections.unmodifiableMap(headers);
    }

    public Map<Label,String> clues() {
        return Collections.unmodifiableMap(clues);
    }

    public List<String> notes() {
        return Collections.unmodifiableList(notes);
    }

    /**
     * Method to write {@link Puzzle} to an {@link OutputStream} in
     * {@link.uri https://github.com/century-arcade/xd target=newtab .xd}
     * format.
     *
     * @param   out             The {@link OutputStream}.
     *
     * @throws  IOException     If the {@link Puzzle} cannot be written.
     */
    public void writeTo(OutputStream out) throws IOException {
        writeTo(new OutputStreamWriter(out, UTF_8));
    }

    /**
     * Method to write {@link Puzzle} to a {@link Writer} in
     * {@link.uri https://github.com/century-arcade/xd target=newtab .xd}
     * format.
     *
     * @param   out             The {@link OutputStream}.
     *
     * @throws  IOException     If the {@link Puzzle} cannot be written.
     */
    public void writeTo(Writer out) throws IOException {
        writeTo((out instanceof PrintWriter)
                    ? ((PrintWriter) out)
                    : new PrintWriter(out));
    }

    /**
     * Method to write {@link Puzzle} to a {@link PrintWriter} in
     * {@link.uri https://github.com/century-arcade/xd target=newtab .xd}
     * format.
     *
     * @param   out             The {@link OutputStream}.
     *
     * @throws  IOException     If the {@link Puzzle} cannot be written.
     */
    public void writeTo(PrintWriter out) throws IOException {
        Map<String,String> headers = headers();

        for (String key : headers.keySet()) {
            String value = headers.get(key);

            if (! StringUtils.isEmpty(value)) {
                out.println(key + COLON + SPACE + value);
            }
        }

        out.println(EMPTY);
        out.println(EMPTY);

        for (CoordinateMap<Cell> row : rows()) {
            for (Cell cell : row.values()) {
                out.print(cell);
            }

            out.println();
        }

        out.println(EMPTY);

        Direction last = null;

        for (Map.Entry<Label,String> entry : clues.entrySet()) {
            if (! entry.getKey().getDirection().equals(last)) {
                out.println(EMPTY);
            }

            last = entry.getKey().getDirection();

            out.println(entry.getKey().toString() + DOT
                        + SPACE + entry.getValue()
                        + SPACE + TILDE + SPACE
                        + answers.get(entry.getKey()));
        }

        if (! notes.isEmpty()) {
            out.println(EMPTY);
            out.println(EMPTY);

            for (String note : notes) {
                out.println(note);
            }
        }
    }

    @Override
    public String toString() { return super.toString(); }

    /**
     * Static method to load an
     * {@link.uri https://github.com/century-arcade/xd target=newtab .xd}
     * resource or {@link File} into a {@link Puzzle}.
     *
     * @param   path            The resource path.
     *
     * @return  The {@link Puzzle}.
     *
     * @throws  IOException     If the path cannot be parsed.
     */
    public static Puzzle load(String path) throws IOException {
        Puzzle puzzle = null;
        InputStream in = null;

        try {
            in = Puzzle.class.getResourceAsStream(path);

            if (in == null) {
                in = new FileInputStream(path);
            }

            List<String> lines =
                new BufferedReader(new InputStreamReader(in, UTF_8))
                .lines()
                .map(t -> t.trim())
                .collect(Collectors.toList());
            TreeMap<Integer,List<String>> sections = new TreeMap<>();
            List<String> BOUNDARY = Arrays.asList(EMPTY, EMPTY);

            while (! lines.isEmpty()) {
                int index = Collections.indexOfSubList(lines, BOUNDARY);

                if (! (index < 0)) {
                    sections.put(sections.size(), lines.subList(0, index));
                    lines = lines.subList(index + BOUNDARY.size(), lines.size());
                } else {
                    sections.put(sections.size(), lines);
                    break;
                }
            }

            puzzle =
                new Puzzle(sections.getOrDefault(0, Collections.emptyList()),
                           sections.getOrDefault(1, Collections.emptyList()),
                           sections.getOrDefault(2, Collections.emptyList()),
                           sections.getOrDefault(3, Collections.emptyList()));
        } catch (UncheckedIOException exception) {
            throw exception.getCause();
        } finally {
            if (in != null) {
                in.close();
            }
        }

        return puzzle;
    }

    private class Solution extends CoordinateMap<Cell> {
        private static final long serialVersionUID = -1328439848464058419L;

        public Solution() { super(Cell.class); }

        public Label label() {
            return new Label(getDirection(),
                             indices.indexOf(getCoordinate()) + 1);
        }

        public Direction getDirection() {
            Direction direction = null;

            if (getRowCount() == 1) {
                direction = Direction.ACROSS;
            } else /* if (getColumnCount() == 1) */ {
                direction = Direction.DOWN;
            }

            return direction;
        }

        public Coordinate getCoordinate() { return firstKey(); }

        @Override
        public String toString() {
            return (values().stream()
                    .map(t -> t.toString())
                    .collect(Collectors.joining()));
        }
    }
}
