import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.stream.Stream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public abstract class BaseCheck {
    protected final Random random = new Random();
    private int test = 0;

    protected abstract void testAll();

    protected void test(String description, Runnable action) {
        test++;
        System.out.println("Test " + test + ": " + description);
        action.run();
        System.out.println("ok");
    }

    protected char randomChar(String chars) {
        return chars.charAt(random.nextInt(chars.length()));
    }

    protected String randomWord(String alphabet) {
        final StringBuilder sb = new StringBuilder();
        do {
            sb.append(randomChar(alphabet));
        } while (random.nextBoolean());
        return sb.toString();
    }

    protected String[] randomWords(int n, String alphabet) {
        return Stream.generate(() -> randomWord(alphabet)).limit(random.nextInt(n) + 1).toArray(String[]::new);
    }

    protected void assertEquals(Object expected, Object found) {
        if (!expected.equals(found)) {
            throw error("\nExpected " + expected + "\nfound    " + found, null);
        }
    }

    protected AssertionError error(String message, Exception e) {
        return new AssertionError("Test " + test + ": " + message, e);
    }

    interface Action {
        void perform() throws Exception;
    }

    interface Processor<T, R> {
        R process(T t) throws Exception;
    }

    protected void run(Action action) {
        try {
            long start = System.currentTimeMillis();
            action.perform();
            System.out.println("    Finished in " + (System.currentTimeMillis() - start) + " ms");
        } catch (Exception e) {
            throw error("Program thrown exception", e);
        }
    }


    protected <T> T read(Processor<Scanner, T> processor) {
        try {
            try (Scanner in = new Scanner(Files.newBufferedReader(getPath("out")))) {
                return processor.process(in);
            }
        } catch (Exception e) {
            throw error("Cannot read output", e);
        }
    }

    protected <T> T write(Processor<PrintWriter, T> processor) {
        try {
            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(getPath("in")))) {
                return processor.process(writer);
            }
        } catch (Exception e) {
            throw error("Error writing input file", e);
        }
    }

    private Path getPath(String suffix) {
        final String name = this.getClass().getSimpleName().toLowerCase();
        return Paths.get(name.substring(name.length() - 1) + "." + suffix);
    }


    protected List<Integer> readInts() {
        return read(in -> {
            List<Integer> found = new ArrayList<>();
            while (in.hasNext()) {
                found.add(in.nextInt());
            }
            return found;
        });
    }
}
