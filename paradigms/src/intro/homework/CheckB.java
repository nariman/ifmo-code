import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class CheckB {
    public static final Path INPUT_FILE = Paths.get("b.in");
    public static final Path OUTPUT_FILE = Paths.get("b.out");
    public static final Random random = new Random();
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static int test = 0;

    public static void main(String[] args) {
        check("Single number", false, "1");
        check("Single word", false, "hello");
        check("Multiple numbers", false, "1", "+2", "-3");
        check("Multiple words", false, "hello", "ciao", "salute");
        check("Words and numbers", false, "1", "hello", "2", "ciao");
        check("Crazy spaces", true, "1", "one", "2", "two", "3", "three", "4", "four", "5", "five");
        check("Overflow", true, "" + Integer.MIN_VALUE, "" + Integer.MIN_VALUE);

        for (int n = 10; n <= 1_000_000; n *= 10) {
            random(n);
        }
    }

    public static boolean isInteger(String s) {
        return s.matches("[-+]?\\d+");
    }

    private static void random(int n) {
        check(
                String.format("Random items=%d", n),
                true,
                Stream.generate(() -> random.nextBoolean() ? "" + random.nextInt() : randomWord(ALPHABET)).limit(random.nextInt(n)).toArray(String[]::new)
        );
    }

    private static String randomWord(String alphabet) {
        final StringBuilder sb = new StringBuilder();
        do {
            sb.append(alphabet.charAt(random.nextInt(alphabet.length())));
        } while (random.nextBoolean());
        return sb.toString();
    }

    private static void check(String description, boolean crazy, String... values) {
        test++;
        System.out.println("Test " + test + ": " + description);
        write(crazy, values);
        run();
        readAndCheck(
                Arrays.stream(values).filter(CheckB::isInteger).mapToLong(Long::parseLong).sum(),
                Arrays.stream(values).filter(((Predicate<String>) CheckB::isInteger).negate()).collect(Collectors.joining())
        );
        System.out.println("ok");
    }

    private static void readAndCheck(long expectedValue, String expectedString) {
        try {
            try (Scanner in = new Scanner(Files.newBufferedReader(OUTPUT_FILE))) {
                assertEquals(expectedValue, in.nextLong());
                assertEquals(expectedString, in.hasNext() ? in.next() : "");
            }
        } catch (IOException e) {
            throw error("Cannot read output", e);
        }
    }

    private static void assertEquals(Object expected, Object found) {
        if (!expected.equals(found)) {
            throw error("\nExpected " + expected + "\nfound    " + found, null);
        }
    }

    private static void run() {
        try {
            long start = System.currentTimeMillis();
            B.main(null);
            System.out.println("    Finished in " + (System.currentTimeMillis() - start) + " ms");
        } catch (Exception e) {
            throw error("Program thrown exception", e);
        }
    }

    private static void write(boolean crazy, String[] values) {
        try {
            try (BufferedWriter writer = Files.newBufferedWriter(INPUT_FILE)) {
                boolean first = true;
                for (String value : values) {
                    writer.write(first && random.nextBoolean() ? "" : randomSpace(crazy));
                    first = false;
                    writer.write(value);
                }
                if (random.nextBoolean()) {
                    writer.write(randomSpace(crazy));
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            throw error("Error writing input file", e);
        }
    }

    private static AssertionError error(String message, Exception e) {
        return new AssertionError("Test " + test + ": " + message, e);
    }

    private static String randomSpace(boolean crazy) {
        return !crazy || random.nextBoolean() ? " " : " \t\n".charAt(random.nextInt(3)) + randomSpace(crazy);
    }
}
