package search;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BinarySearchMissingTest extends BaseBinarySearchTest {
    public static void main(final String[] args) {
        test("BinarySearchMissing", (a, x) -> {
            for (int i = 0; i < a.length; i++) {
                if (a[i] == x) {
                    return new long[]{i};
                }
                if (x > a[i]) {
                    return new long[]{-1 - i};
                }
            }
            return new long[]{-1 - a.length};
        });
    }
}
