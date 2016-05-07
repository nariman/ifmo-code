import java.util.*;
import java.util.stream.Collectors;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class CheckE extends BaseCheck {
    public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ-'";
    public static final String DELIMITERS = " \t\n\t~!@#$%^&*()_+=,./\\|";

    public static void main(String[] args) {
        new CheckE().testAll();
    }

    @Override
    protected void testAll() {
        check("Single word", "hello");
        check("Multiple words", "hello", "ciao", "salute");
        check("Duplicate words", "hello", "hello", "hello");
        check("Duplicate words (casing)", "hello", "Hello", "HELLO");
        check("Word with hyphen", "New-York", "NEW-YORK");
        check("Word with apostrophe", "No", "you", "can't");

        for (int n = 10; n <= 1_000_000; n *= 10) {
            random(n);
        }
    }

    private void random(int n) {
        check(
                String.format("Random items=%d", n),
                randomWords(n, ALPHABET)
        );
    }

    private List<Pair> read() {
        return read(in -> {
            final List<Pair> answer = new ArrayList<>();
            while (in.hasNext()) {
                int count = in.nextInt();
                answer.add(new Pair(in.next(), count));
            }
            return answer;
        });
    }

    private void write(String[] values) {
        write(writer -> {
            boolean first = true;
            for (String value : values) {
                writer.write(first && random.nextBoolean() ? "" : randomWord(DELIMITERS));
                first = false;
                writer.write(value);
            }
            if (random.nextBoolean()) {
                writer.write(randomWord(DELIMITERS));
            }
            writer.write("\n");
            return null;
        });
    }

    private void check(String description, String... values) {
        test(description, () -> {
            write(values);
            List<Pair> expected = Arrays.stream(values)
                    .map(String::toLowerCase)
                    .collect(Collectors.groupingBy(e -> e, Collectors.reducing(0, e -> 1, Integer::sum)))
                    .entrySet().stream()
                    .sorted(Comparator
                            .<Map.Entry<String, Integer>>comparingInt(Map.Entry::getValue).reversed()
                            .thenComparing(Map.Entry::getKey)).map(e -> new Pair(e.getKey(), e.getValue()))
                    .collect(Collectors.toList());
            run(() -> E.main(null));
            assertEquals(expected, read());
        });
    }

    static class Pair {
        final String word;
        final int count;

        public Pair(String word, int count) {
            this.word = word;
            this.count = count;
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof Pair) {
                Pair that = (Pair) o;
                return this.count == that.count && this.word.equals(that.word);
            }
            return false;
        }

        @Override
        public int hashCode() {
            int result = word.hashCode();
            result = 31 * result + count;
            return result;
        }

        @Override
        public String toString() {
            return String.format("(%d, %s)", count, word);
        }
    }
}
