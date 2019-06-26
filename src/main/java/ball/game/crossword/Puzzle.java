/*
 * $Id$
 *
 * Copyright 2019 Allen D. Ball.  All rights reserved.
 */
package ball.game.crossword;

import ball.util.Coordinate;
import ball.util.CoordinateMap;
import ball.util.DispatchSpliterator;
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
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.SortedMap;
import java.util.Spliterator;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
    private static final long serialVersionUID = -986536680348801165L;

    private static final List<String> BOUNDARY = Arrays.asList(EMPTY, EMPTY);

    private static final String COLON = ":";
    private static final String DOT = ".";
    private static final String TILDE = "~";

    private final Puzzle parent;
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
     * @param   parent          The source {@link Puzzle}.
     * @param   solution        The {@link Solution} to update.
     * @param   sequence        The {@link CharSequence} for the
     *                          {@link Solution}.
     */
    private Puzzle(Puzzle parent, Solution solution, CharSequence sequence) {
        this(parent);

        try {
            for (Coordinate coordinate : solution) {
                put(coordinate, parent.get(coordinate).clone());
            }

            solution.setSolution(this, sequence);
        } catch (Exception exception) {
            throw new ExceptionInInitializerError(exception);
        }
    }

    /**
     * Private constructor for {@link #clone()}.
     *
     * @param   parent          The source {@link Puzzle}.
     */
    private Puzzle(Puzzle parent) {
        super(Cell.class);

        this.parent = parent;
        this.headers = parent.headers;
        this.indices = parent.indices;
        this.solutions = parent.solutions;
        this.clues = parent.clues;
        this.notes = parent.notes;

        this.putAll(parent);
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

        this.parent = null;

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
                throw new IllegalArgumentException(coordinate.toString() + ": "
                                                   + cell.toString()
                                                   + " -> " + character.toString());
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

    /**
     * Method to solve a {@link Puzzle}.
     *
     * @param   dictionary      The {@link Set} of possible solutions.
     *
     * @return  A {@link Stream} of possible solutions.
     */
    public Stream<Puzzle> solve(Set<CharSequence> dictionary) {
        return StreamSupport.stream(new Solver(this, dictionary), false);
    }

    private boolean isChanged(Coordinate coordinate) {
        return (parent == null || (get(coordinate) != parent.get(coordinate)));
    }

    private SortedMap<Coordinate,Cell> changed() {
        SortedMap<Coordinate,Cell> map =
            entrySet()
            .stream()
            .filter(t -> parent != null)
            .filter(t -> t.getValue() != parent.get(t.getKey()))
            .collect(Collectors.toMap(Map.Entry::getKey,
                                      Map.Entry::getValue,
                                      (v0, v1) -> v0,
                                      TreeMap::new));

        return map;
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

        private Solution(Collection<Coordinate> collection) {
            super(collection);
        }

        /**
         * Method to get the {@link Solution} in a {@link Puzzle}.
         *
         * @param       puzzle  The {@link Puzzle}.
         *
         * @return      The {@link Solution} {@link CharSequence}.
         */
        public CharSequence getSolution(Puzzle puzzle) {
            return (stream()
                    .map(t -> puzzle.get(t))
                    .map(Object::toString)
                    .collect(Collectors.joining()));
        }

        private void setSolution(Puzzle puzzle, CharSequence solution) {
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

        private Pattern asPattern(Puzzle puzzle) {
            String pattern = getSolution(puzzle).toString().toUpperCase();

            return Pattern.compile(pattern);
        }

        private List<Coordinate> unsolved(Puzzle puzzle) {
            return (stream()
                    .filter(t -> (! puzzle.get(t).isSolved()))
                    .collect(Collectors.toList()));
        }
    }

    private static class Solver extends DispatchSpliterator<Puzzle> {
        private final Puzzle puzzle;
        private final Set<CharSequence> dictionary;

        public Solver(Puzzle puzzle, Set<CharSequence> dictionary) {
            super(Integer.MAX_VALUE, Spliterator.NONNULL);

            this.puzzle = Objects.requireNonNull(puzzle);
            this.dictionary = Objects.requireNonNull(dictionary);
        }

        @Override
        protected Spliterator<Supplier<Spliterator<Puzzle>>> spliterators() {
            List<Supplier<Spliterator<Puzzle>>> list = new LinkedList<>();
            Solution solution =
                puzzle.solutions().values()
                .stream()
                .filter(t -> (! t.isSolved(puzzle)))
                .sorted(Comparator
                        .<Solution>comparingInt(t -> t.stream().map(c -> puzzle.get(c)).anyMatch(Cell::isSolved) ? +1 : -1)
                        .thenComparingInt(t -> t.unsolved(puzzle).size())
                        .thenComparingInt(Solution::size).reversed())
                .findFirst().orElse(null);

            if (solution != null) {
                Pattern pattern = solution.asPattern(puzzle);
                List<CharSequence> sequences =
                    dictionary.stream()
                    .filter(t -> pattern.matcher(t).matches())
                    .collect(Collectors.toList());

                for (CharSequence sequence : sequences) {
                    Puzzle child = new Puzzle(puzzle, solution, sequence);
                    Set<Coordinate> changed = child.changed().keySet();
                    boolean verified =
                        child.solutions().values()
                        .stream()
                        .filter(t -> (! Collections.disjoint(t, changed)))
                        .filter(t -> t.isSolved(child))
                        .allMatch(t -> dictionary.contains(t.getSolution(child)));

                    if (verified) {
                        list.add(() -> new Solver(child, dictionary));
                    }
                }
            }

            if (list.isEmpty()) {
                list.add(() -> Stream.of(puzzle).spliterator());
            }

            return list.spliterator();
        }

        @Override
        public String toString() { return super.toString(); }
    }
}
