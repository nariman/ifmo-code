package homework.prev.nariman;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class CheckC {
    public static final Path INPUT_FILE = Paths.get("c.in");
    public static final Path OUTPUT_FILE = Paths.get("c.out");
    public static final Random random = new Random();
    public static final String ALPHABET = " \tabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.+-";
    private static final double EPS = 1e-10;
    public static int test = 0;

    public static void main(String[] args) {
        check("Single integer", " ", "1");
        check("Single double", " ", "1.1");
        check("Multiple integers", " ", "1", "+2", "-3");
        check("Multiple doubles", " ", "1.1", "1e-3", "1e+3");
        check("Integers and doubles", " ", "1", "1.1", "2", "1e-3");
        check("Point as delimiter", ".", "1", "1.1", "2", "1e-3");
        check("eE as delimiter", "eE", "1", "1.1", "2", "1e-3", "1E-3");

        for (int n = 10; n <= 1_000_000; n *= 10) {
            for (int m = 10; m <= 1_000_000; m *= 10) {
                random(n, m);
            }
        }
    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private static void random(int n, int m) {
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < m; i++) {
            sb.append(randomChar(ALPHABET));
        }
        if (sb.indexOf("-") >=0) {
            sb.append("eE");
        }
        check(
                String.format("Random values=%d", n),
                sb.toString(),
                Stream.generate(() -> random.nextBoolean() ? "" + random.nextInt() : "" + randomDouble()).limit(random.nextInt(n)).toArray(String[]::new)
        );
    }

    private static double randomDouble() {
        return (random.nextDouble() * 2 - 1) * Math.pow(10, (double) (random.nextInt(200) - 100));
    }

    private static void check(String description, String delims, String... values) {
        test++;
        System.out.println("Test " + test + ": " + description);
        System.out.println("    delimiters: " + crop(delims, 30));
        write(delims, values);
        run(delims);

        final Pattern pattern = Pattern.compile("[" + (delims.contains("-") ? delims.replace("-", "") + "-" : delims) + "]");
        final String[] parts = Arrays.stream(values).flatMap(z -> Arrays.stream(pattern.split(z))).filter(not(String::isEmpty)).toArray(String[]::new);
        readAndCheck(
                Arrays.stream(parts).filter(homework.prev.dima.CheckC::isInteger).mapToLong(Long::parseLong).sum(),
                Arrays.stream(parts).filter(not(homework.prev.dima.CheckC::isInteger)).mapToDouble(Double::parseDouble).sum()
        );
        System.out.println("ok");
    }

    private static String crop(String s, int size) {
        return s.length() <= 2 * size + 10 ? s : s.substring(0, size) + "<" + (s.length() - size * 2) + " chars>" + s.substring(s.length() - size);
    }

    static <T> Predicate<T> not(Predicate<T> p) {
        return p.negate();
    }

    private static void readAndCheck(long expectedValue, double expectedDouble) {
        try {
            try (Scanner in = new Scanner(Files.newBufferedReader(OUTPUT_FILE))) {
                in.useLocale(Locale.US);
                assertEquals(expectedValue, in.nextLong());
                double actualDouble = in.nextDouble();
                double d = Math.abs(expectedDouble - actualDouble);
                if (d >= EPS && d / actualDouble >= EPS) {
                    assertEquals(expectedDouble, actualDouble);
                }
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

    private static void run(String delims) {
        try {
            long start = System.currentTimeMillis();
            homework.prev.dima.C.main(new String[]{delims});
            System.out.println("    Finished in " + (System.currentTimeMillis() - start) + " ms");
        } catch (Exception e) {
            throw error("Program thrown exception", e);
        }
    }

    private static void write(String delims, String[] values) {
        try {
            try (BufferedWriter writer = Files.newBufferedWriter(INPUT_FILE)) {
                boolean first = true;
                for (String value : values) {
                    writer.write(first && random.nextBoolean() ? "" : randomSpace(delims));
                    first = false;
                    writer.write(value);
                }
                if (random.nextBoolean()) {
                    writer.write(randomSpace(delims));
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

    private static String randomSpace(String delims) {
        return randomChar(delims) + (random.nextBoolean() ? "" : randomSpace(delims));
    }

    private static char randomChar(String str) {
        return str.charAt(random.nextInt(str.length()));
    }
}
