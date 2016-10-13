import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class CheckD {
    public static final Path INPUT_FILE = Paths.get("d.in");
    public static final Path OUTPUT_FILE = Paths.get("d.out");
    public static final Random random = new Random();
    public static int test = 0;

    public static void main(String[] args) {
        for (int n = 10; n <= 100_000; n *= 10) {
            for (int follow = 0; follow <= 10; follow += 2) {
                for (int push = 1; push <= 10; push += 2) {
                    random(n, follow * 0.1, push);
                }
            }
        }
    }

    private static void random(int n, double follow, int push) {
        test++;
        System.out.println("Test " + test + ": n=" + n);

        List<Integer> expected = generate(n, follow, push);
        run();
        assertEquals(expected, read());
    }

    private static List<Integer> generate(int n, double follow, int push) {
        try {
            try (PrintWriter writer = new PrintWriter(Files.newBufferedWriter(INPUT_FILE))) {
                List<Integer> results = new ArrayList<>();
                List<PersistentStack> stacks = new ArrayList<>();
                stacks.add(new PersistentStack());
                for (int i = 0; i < n; i++) {
                    int version = random.nextDouble() <= follow ? i : random.nextInt(i + 1);
                    PersistentStack stack = stacks.get(version);
                    switch (stack.getSize() == 0 ? 4 : random.nextInt(3 + push)) {
                        case 0:
                            writer.println("- " + version);
                            results.add(stack.getValue());
                            stacks.add(stack.pop());
                            break;
                        case 1:
                            writer.println("max " + version);
                            results.add(stack.getMax());
                            stacks.add(stack);
                            break;
                        case 2:
                            writer.println("min " + version);
                            results.add(stack.getMin());
                            stacks.add(stack);
                            break;
                        default:
                            int value = random.nextInt();
                            writer.println("+ " + version + " " + value);
                            results.add(stack.getSize() + 1);
                            stacks.add(stack.push(value));
                    }
                }
                return results;
            }
        } catch (IOException e) {
            throw error("Error writing input file", e);
        }
    }

    private static void run() {
        try {
            long start = System.currentTimeMillis();
            D.main(null);
            System.out.println("    Finished in " + (System.currentTimeMillis() - start) + " ms");
        } catch (Exception e) {
            throw error("Program thrown exception", e);
        }
    }

    private static List<Integer> read() {
        try {
            try (Scanner in = new Scanner(Files.newBufferedReader(OUTPUT_FILE))) {
                List<Integer> found = new ArrayList<>();
                while (in.hasNext()) {
                    found.add(in.nextInt());
                }
                return found;
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

    private static AssertionError error(String message, Exception e) {
        return new AssertionError("Test " + test + ": " + message, e);
    }
}
