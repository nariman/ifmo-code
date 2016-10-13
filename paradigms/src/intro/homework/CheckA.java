import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class CheckA  {
    public static final Path INPUT_FILE = Paths.get("a.in");
    public static final Path OUTPUT_FILE = Paths.get("a.out");
    public static final Random random = new Random();
    public static int test = 0;

    public static void main(String[] args) {
        assertEquals("Single number", false, new int[][]{new int[]{1}});
        assertEquals("Single line", false, new int[][]{new int[]{1, 2, 3}});
        assertEquals("Single number per line", false, new int[]{1}, new int[]{2}, new int[]{3});
        assertEquals("Multiple numbers per line", false, new int[]{1}, new int[]{1, 2}, new int[]{1, 2, 3});
        assertEquals("Negative numbers", false, new int[]{-1}, new int[]{-1, 2}, new int[]{1, -2, -3});
        assertEquals("Empty lines", false, new int[]{}, new int[]{}, new int[]{});
        assertEquals("Crazy empty lines", true, new int[]{}, new int[]{}, new int[]{});
        assertEquals("Crazy spaces", true, new int[]{-1}, new int[]{-1, 2}, new int[]{1, -2, -3});
        assertEquals("Overflow", true, new int[]{Integer.MAX_VALUE, Integer.MAX_VALUE}, new int[]{Integer.MIN_VALUE, Integer.MIN_VALUE});

        for (int n : new int[]{10, 100, 1000, 10000}) {
            for (int m : new int[]{10, 100, 1000}) {
                for (int v : new int[]{10, 1000, 1000_000, Integer.MAX_VALUE}) {
                    random(n, m, v);
                }
            }
        }
    }

    private static void random(int n, int m, int v) {
        int[][] valuess = new int[random.nextInt(n) + 1][];
        for (int i = 0; i < valuess.length; i++) {
            valuess[i] = random.ints(random.nextInt(m + 1)).map(j -> j % v).toArray();
        }
        assertEquals(String.format("Random lines=%d, numbers=%d, max=%d", n, m, v), true, valuess);
    }

    private static void assertEquals(String description, boolean crazy, int[]... valuess) {
        test++;
        System.out.println("Test " + test + ": " + description);
        write(crazy, valuess);
        run();
        assertEquals(Arrays.stream(valuess).map(as -> Arrays.stream(as).mapToLong(v -> (long) v).sum()).collect(Collectors.toList()), read());
    }

    private static void assertEquals(Object expected, Object found) {
        if (!expected.equals(found)) {
            throw error("\nExpected " + expected + "\nfound    " + found, null);
        }
        System.out.println("ok");
    }

    private static List<Long> read() {
        try {
            try (Scanner in = new Scanner(Files.newBufferedReader(OUTPUT_FILE))) {
                List<Long> found = new ArrayList<>();
                while (in.hasNext()) {
                    found.add(in.nextLong());
                }
                return found;
            }
        } catch (IOException e) {
            throw error("Cannot read output", e);
        }
    }

    private static void run() {
        try {
            long start = System.currentTimeMillis();
            A.main(null);
            System.out.println("    Finished in " + (System.currentTimeMillis() - start) + " ms");
        } catch (Exception e) {
            throw error("Program thrown exception", e);
        }
    }

    private static void write(boolean crazy, int[][] valuess) {
        try {
            try (BufferedWriter writer = Files.newBufferedWriter(INPUT_FILE)) {
                for (int[] values : valuess) {
                    boolean first = true;
                    for (int value : values) {
                        writer.write(first && random.nextBoolean() ? "" : randomSpace(crazy));
                        first = false;
                        writer.write("" + value);
                    }
                    if (random.nextBoolean()) {
                        writer.write(randomSpace(crazy));
                    }
                    writer.write("\n");
                }
            }
        } catch (IOException e) {
            throw error("Error writing input file", e);
        }
    }

    private static AssertionError error(String message, Exception e) {
        return new AssertionError("Test " + test + ": " + message, e);
    }

    private static String randomSpace(boolean crazy) {
        return !crazy || random.nextBoolean() ? " " : " \t".charAt(random.nextInt(2)) + randomSpace(crazy);
    }
}
