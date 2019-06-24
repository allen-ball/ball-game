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
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Crossword {@link Puzzle}.
 *
 * @author {@link.uri mailto:ball@hcf.dev Allen D. Ball}
 * @version $Revision$
 */
public class Puzzle extends CoordinateMap<Cell> implements Cloneable {
    private static final long serialVersionUID = 4904996395112191828L;

    private static final List<String> BOUNDARY = Arrays.asList(EMPTY, EMPTY);

    private static final String COLON = ":";
    private static final String DOT = ".";
    private static final String TILDE = "~";

    /** @serial */
    private final Map<String,String> headers;
    /** @serial */
    private final List<Coordinate> indices;
    /** @serial */
    private final SortedMap<Label,Solution> solutions;
    /** @serial */
    private final SortedMap<Label,String> clues;
    /** @serial */
    private final List<String> notes;

    /**
     * Private constructor for {@link #clone()}.
     *
     * @param   headers         The source {@link Puzzle}.
     */
    private Puzzle(Puzzle puzzle) {
        super(Cell.class);

        this.headers = puzzle.headers;
        this.indices = puzzle.indices;
        this.solutions = puzzle.solutions;
        this.clues = puzzle.clues;
        this.notes = puzzle.notes;

        puzzle.entrySet()
            .stream()
            .forEach(t -> put(t.getKey(), t.getValue()));
    }

    /**
     * Private constructor for {@link #load(String)}.
     *
     * @param   headers         The {@link List} of header {@link String}s.
     * @param   grid            The {@link List} of grid row
     *                          {@link String}s.
     * @param   clues           The {@link List} of clue {@link String}s.
     * @param   notes           The {@link List} of note {@link String}s.
     */
    private Puzzle(List<String> headers, List<String> grid,
                   List<String> clues, List<String> notes) {
        super(Cell.class);

        this.headers =
            headers.stream()
            .filter(t -> isNotBlank(t))
            .map(t -> t.split(Pattern.quote(COLON), 2))
            .filter(t -> isNotBlank(t[0]))
            .collect(Collectors.toMap(k -> k[0].trim(),
                                      v -> (v.length > 1) ? v[1].trim() : EMPTY,
                                      (v0, v1) -> isNotBlank(v0) ? v0 : v1,
                                      OrderedHeaders::new));
        this.headers.values().removeIf(t -> isBlank(t));

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

        List<CoordinateMap<Cell>> groups = groups();

        this.indices =
            groups.stream()
            .map(t -> t.firstKey())
            .collect(Collectors.toCollection(TreeSet::new))
            .stream()
            .collect(Collectors.toList());

        this.solutions =
            groups.stream()
            .collect(Collectors.toMap(k -> labelFor(k),
                                      v -> new Solution(v.keySet()),
                                      (v0, v1) -> v0, TreeMap::new));

        this.clues =
            clues.stream()
            .filter(t -> isNotBlank(t))
            .map(t -> t.split("[. ]+", 2))
            .collect(Collectors.toMap(k -> Label.parse(k[0]),
                                      v -> v[1].split("[~]", 2)[0].trim(),
                                      (v0, v1) -> isNotBlank(v0) ? v0 : v1,
                                      TreeMap::new));
        this.solutions.keySet()
            .stream()
            .forEach(t -> this.clues.computeIfAbsent(t, k -> "TBD"));

        this.notes =
            (notes != null)
                ? notes.stream().collect(Collectors.toList())
                : Collections.emptyList();
    }

    private List<CoordinateMap<Cell>> groups() {
        List<CoordinateMap<Cell>> list = new ArrayList<>();

        for (CoordinateMap<Cell> line :
                 Stream.concat(rows().stream(), columns().stream())
                 .collect(Collectors.toList())) {
            list.add(new CoordinateMap<Cell>(Cell.class));

            for (Map.Entry<Coordinate,Cell> entry : line.entrySet()) {
                if (! entry.getValue().isBlock()) {
                    list.get(list.size() - 1)
                        .put(entry.getKey(), entry.getValue());
                } else {
                    list.add(new CoordinateMap<Cell>(Cell.class));
                }
            }
        }

        list.removeIf(t -> ! (t.size() > 1));

        return list;
    }

    private Label labelFor(CoordinateMap<Cell> map) {
        Direction direction = null;

        if (map.getRowCount() > 1) {
            direction = Direction.DOWN;
        } else if (map.getColumnCount() > 1) {
            direction = Direction.ACROSS;
        } else {
            throw new IllegalArgumentException();
        }

        return new Label(direction, indices.indexOf(map.firstKey()) + 1);
    }

    public Map<String,String> headers() {
        return Collections.unmodifiableMap(headers);
    }

    public Map<Label,Solution> solutions() {
        return Collections.unmodifiableMap(solutions);
    }

    public Map<Label,String> clues() {
        return Collections.unmodifiableMap(clues);
    }

    public List<String> notes() {
        return Collections.unmodifiableList(notes);
    }

    /**
     * Method to set the solution in a {@link Puzzle}.
     *
     * @param   coordinate      The {@link Coordinate} of the {@link Cell}..
     * @param   character       The solution {@link Character}.
     *
     * @throws  IllegalArgumentException
     *                          If any of the {@link Cell}s are already with
     *                          a different {@link Character} than specified
     *                          in the {@link String}.
     */
    public void setSolution(Coordinate coordinate, Character character) {
        Cell cell = get(coordinate);

        if (cell == null) {
            throw new IllegalStateException();
        }

        if (! cell.isSolved()) {
            cell.setSolution(character);
        } else {
            if (! character.equals(cell.getSolution())) {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * Method to determine if {@link.this} {@link Puzzle} is solved.
     *
     * @return      {@code true} if the {@link Puzzle} is complete;
     *              {@code false} otherwise.
     */
    public boolean isSolved() {
        return values().stream().allMatch(Cell::isSolved);
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
        for (Map.Entry<String,String> entry : headers().entrySet()) {
            if (isNotBlank(entry.getValue())) {
                out.println(entry.getKey() + COLON + SPACE + entry.getValue());
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
                        + solutions.get(entry.getKey()).getSolution(this));
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
    public Puzzle clone() throws CloneNotSupportedException {
        return new Puzzle(this);
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

    private static class OrderedHeaders extends LinkedHashMap<String,String> {
        private static final long serialVersionUID = 6300431553107515101L;

        private static final List<String> HEADERS =
            Arrays.asList("Title", "Author", "Editor",
                          "Special", "Rebus", "Date");

        public OrderedHeaders() {
            super();

            for (String key : HEADERS) {
                put(key, EMPTY);
            }
        }
    }

    /**
     * {@link Puzzle} {@link Solution}
     */
    public static class Solution extends ArrayList<Coordinate> {
        private static final long serialVersionUID = -5859711239104123639L;

        /**
         * Sole constructor.
         *
         * @param       collection
         *                      The {@link Collection} of
         *                      {@link Coordinate}s
         *                      where {@link.this} {@link Solution} is
         *                      located in a {@link Puzzle}.
         */
        protected Solution(Collection<Coordinate> collection) {
            super(collection);
        }

        /**
         * Method to get the {@link Solution} in a {@link Puzzle}.
         *
         * @param       puzzle  The {@link Puzzle}.
         *
         * @return      The {@link String} representation of the
         *              {@link Solution}.
         */
        public String getSolution(Puzzle puzzle) {
            return (stream()
                    .map(t -> puzzle.get(t))
                    .map(Object::toString)
                    .collect(Collectors.joining()));
        }

        /**
         * Method to set the {@link Solution} in a {@link Puzzle}.
         *
         * @param       puzzle  The {@link Puzzle}.
         * @param       solution
         *                      The solution {@link String}.
         */
        public void setSolution(Puzzle puzzle, String solution) {
            if (solution.length() != size()) {
                throw new IllegalArgumentException();
            }

            for (int i = 0; i < size(); i += 1) {
                puzzle.setSolution(get(i), solution.charAt(i));
            }
        }

        /**
         * Method to determine if {@link.this} {@link Solution} is solved.
         *
         * @param       puzzle  The {@link Puzzle}.
         *
         * @return      {@code true} if the {@link Solution} is complete;
         *              {@code false} otherwise.
         */
        public boolean isSolved(Puzzle puzzle) {
            return stream().map(t -> puzzle.get(t)).allMatch(Cell::isSolved);
        }

        /**
         * Method to determine if {@link.this} {@link Solution} is partially
         * solved.
         *
         * @param       puzzle  The {@link Puzzle}.
         *
         * @return      {@code true} if the {@link Solution} is partially
         *              complete; {@code false} otherwise.
         */
        public boolean isPartial(Puzzle puzzle) {
            return stream().map(t -> puzzle.get(t)).anyMatch(Cell::isSolved);
        }
    }
}
