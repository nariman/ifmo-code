import java.util.ArrayList;
import java.util.List;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class CheckD extends BaseCheck {
    public static void main(String[] args) {
        new CheckD().testAll();
    }

    @Override
    protected void testAll() {
        for (int n = 10; n <= 100_000; n *= 10) {
            for (int follow = 0; follow <= 10; follow += 2) {
                for (int push = 1; push <= 10; push += 2) {
                    random(n, follow * 0.1, push);
                }
            }
        }
    }

    private void random(int n, double follow, int push) {
        test("n=" + n, () -> {
            List<Integer> expected = generate(n, follow, push);
            run(() -> D.main(null));
            assertEquals(expected, readInts());
        });
    }

    private List<Integer> generate(int n, double follow, int push) {
        return write(writer -> {
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
        });
    }
}
