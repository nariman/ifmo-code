import java.util.ArrayList;
import java.util.List;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class CheckF extends BaseCheck {
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-'\t\t~!@#$%^&*()_+=,./\\|";

    public static void main(String[] args) {
        new CheckF().testAll();
    }

    @Override
    protected void testAll() {
        for (int n = 10; n <= 100_000; n *= 10) {
            for (int w = 10; w <= n; w *= 10) {
                random(n, w);
            }
        }
    }

    private void random(int n, int w) {
        test(n + " operations on " + w + " different lines", () -> {
            List<Integer> expected = generate(n, randomWords(w, ALPHABET));
            run(() -> F.main(null));
            assertEquals(expected, readInts());
        });
    }

    private List<Integer> generate(int n, String[] lines) {
        return write(writer -> {
            final MultiSet multiSet = new MultiSet();
            final List<Integer> results = new ArrayList<>();

            for (int i = 0; i < n; i++) {
                final String line = lines[random.nextInt(lines.length)];
                switch (random.nextInt(5)) {
                    case 0:
                        writer.println("+ " + line);
                        multiSet.add(line);
                        break;
                    case 1:
                        writer.println("- " + line);
                        multiSet.remove(line);
                        break;
                    case 2:
                        writer.println("* " + line);
                        results.add(multiSet.query(line));
                        break;
                    case 3:
                        writer.println("?");
                        results.add(multiSet.total());
                        break;
                    case 4:
                        writer.println("??");
                        results.add(multiSet.distinct());
                        break;
                    default:
                        throw new AssertionError("Unexpected value");
                }
            }
            return results;
        });
    }
}
