/*
 * $Id$
 *
 * Copyright 2018 Allen D. Ball.  All rights reserved.
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
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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
 * @author {@link.uri mailto:ball@iprotium.com Allen D. Ball}
 * @version $Revision$
 */
public class Puzzle extends CoordinateMap<Cell> {
    private static final long serialVersionUID = -4620118038566819736L;

    private static final List<String> HEADERS =
        Arrays.asList("Title", "Author", "Editor", "Special", "Rebus", "Date");
    private static final List<String> BOUNDARY = Arrays.asList(EMPTY, EMPTY);

    private static final String COLON = ":";

    /** @serial */
    private final LinkedHashMap<String,String> headers = new LinkedHashMap<>();
    /** @serial */
    private final List<Coordinate> indices;
    /** @serial */
    private final ArrayList<String> clues = new ArrayList<>();
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

        indices =
            list.stream()
            .map(t -> t.getCoordinate())
            .collect(Collectors.toCollection(TreeSet::new))
            .stream()
            .collect(Collectors.toList());
list.stream()
    .forEach(t -> System.out.println(t.label().toString()
                                     + " " + t.getCoordinate().toString()
                                     + " " + t.toString()));
        this.clues.addAll(clues);
        this.notes.addAll(notes);
    }

    public Map<String,String> headers() {
        return Collections.unmodifiableMap(headers);
    }

    public List<String> clues() {
        return Collections.unmodifiableList(clues);
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
        out.println(EMPTY);

        for (String clue : clues) {
            out.println(clue);
        }

        if (! notes.isEmpty()) {
            out.println(EMPTY);
            out.println(EMPTY);

            for (String clue : clues) {
                out.println(clue);
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

    private static class Label implements Comparable<Label> {
        private static final Comparator<? super Label> COMPARATOR =
            Comparator
            .<Label>comparingInt(t -> t.direction.ordinal())
            .thenComparingInt(t -> t.index);

        private final Direction direction;
        private final int index;

        public Label(Direction direction, int index) {
            this.direction = Objects.requireNonNull(direction);
            this.index = index;
        }

        public Direction getDirection() { return direction; }

        public int getIndex() { return index; }

        @Override
        public int compareTo(Label that) {
            return COMPARATOR.compare(this, that);
        }

        @Override
        public boolean equals(Object object) {
            return ((object instanceof Label)
                        ? (this.compareTo((Label) object) == 0)
                        : super.equals(object));
        }

        @Override
        public int hashCode() { return Objects.hash(direction, index); }

        @Override
        public String toString() {
            return (getDirection().toString().substring(0, 1)
                    + String.valueOf(index));
        }
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
