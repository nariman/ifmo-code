package search;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class BinarySearchTest extends BaseBinarySearchTest {
    public static void main(final String[] args) {
        test("BinarySearch", (a, x) -> {
            for (int i = 0; i < a.length; i++) {
                if (a[i] <= x) {
                    return new long[]{i};
                }
            }
            return new long[]{a.length};
        });
    }
}
