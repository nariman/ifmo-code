import java.util.Arrays;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class CheckC extends BaseCheck {
    public static final String ALPHABET = " \tabcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ.+-";
    private static final double EPS = 1e-10;

    public static void main(String[] args) {
        new CheckC().testAll();
    }

    @Override
    protected void testAll() {
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

    public boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private void random(int n, int m) {
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

    private double randomDouble() {
        return (random.nextDouble() * 2 - 1) * Math.pow(10, (double) (random.nextInt(200) - 100));
    }

    private void check(String description, String delims, String... values) {
        test(description, () -> {
            System.out.println("    delimiters: " + crop(delims, 30));
            write(delims, values);
            run(() -> C.main(new String[]{delims}));

            final Pattern pattern = Pattern.compile("[" + (delims.contains("-") ? delims.replace("-", "") + "-" : delims) + "]");
            final String[] parts = Arrays.stream(values).flatMap(z -> Arrays.stream(pattern.split(z))).filter(not(String::isEmpty)).toArray(String[]::new);
            readAndCheck(
                    Arrays.stream(parts).filter(this::isInteger).mapToLong(Long::parseLong).sum(),
                    Arrays.stream(parts).filter(not(this::isInteger)).mapToDouble(Double::parseDouble).sum()
            );
        });
    }

    private static String crop(String s, int size) {
        return s.length() <= 2 * size + 10 ? s : s.substring(0, size) + "<" + (s.length() - size * 2) + " chars>" + s.substring(s.length() - size);
    }

    static <T> Predicate<T> not(Predicate<T> p) {
        return p.negate();
    }

    private void readAndCheck(long expectedValue, double expectedDouble) {
        read(in -> {
            in.useLocale(Locale.US);
            assertEquals(expectedValue, in.nextLong());
            double actualDouble = in.nextDouble();
            double d = Math.abs(expectedDouble - actualDouble);
            if (d >= EPS && d / actualDouble >= EPS) {
                assertEquals(expectedDouble, actualDouble);
            }
            return null;
        });
    }

    private void write(String delimiters, String[] values) {
        write(writer -> {
            boolean first = true;
            for (String value : values) {
                writer.write(first && random.nextBoolean() ? "" : randomWord(delimiters));
                first = false;
                writer.write(value);
            }
            if (random.nextBoolean()) {
                writer.write(randomWord(delimiters));
            }
            writer.println();
            return null;
        });
    }
}
