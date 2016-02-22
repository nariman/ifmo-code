import java.util.Random;

/**
 * Created by woofi on 21.02.2016.
 */
public class BinarySearch {

    /**
     * Finds the minimum index `i` of element, that list[i] <= `x` in `list`, sorted in ascending
     * Search area must contains at least one element
     *
     * Inv: `l` less or equal, than minimum index `i` of element, that list[i] <= x in `list`
     * Inv: `r` bigger, than minimum index `i` of element, that list[i] <= x in `list`
     *
     * @param   list        long[]  array with long integers
     * @param   pivot       long    needed element
     * @param   fromIndex   int     left bound of search area, including element, l >= 0        (Pre)
     * @param   toIndex     int     right bound of area, excluding element, r > 0, r > l        (Pre)
     * @return              int     minimum index `i` of element, that list[i] <= x in `list`,  (Post)
     *                              or `l`, if element not found
     */
    private static int iterative(long[] list, long pivot, int fromIndex, int toIndex) {
        int m;

        while (toIndex - fromIndex > 1) {
            m = fromIndex + (toIndex - fromIndex) / 2;

            if (list[m] <= pivot) {
                toIndex = m;
            } else {
                fromIndex = m;
            }

        }

        if (list[fromIndex] > pivot) {
            return toIndex;
        } else {
            return fromIndex;
        }
    }

    /**
     * Finds the minimum index `i` of element, that list[i] <= `x` in `list`, sorted in ascending
     * Search area must contains at least one element
     *
     * Inv: `l` less or equal, than minimum index `i` of element, that list[i] <= x in `list`
     * Inv: `r` bigger, than minimum index `i` of element, that list[i] <= x in `list`
     *
     * @param   list        long[]  array with long integers
     * @param   pivot       long    needed element
     * @param   fromIndex   int     left bound of search area, including element, l >= 0        (Pre)
     * @param   toIndex     int     right bound of area, excluding element, r > 0, r > l        (Pre)
     * @return              int     minimum index `i` of element, that list[i] <= x in `list`,  (Post)
     *                              or `l`, if element not found
     */
    private static int recursive(long list[], long pivot, int fromIndex, int toIndex) {
        if (toIndex - fromIndex < 2) {
            if (list[fromIndex] > pivot) {
                return toIndex;
            } else {
                return fromIndex;
            }
        }

        int m = fromIndex + (toIndex - fromIndex) / 2;
        if (list[m] <= pivot) {
            return recursive(list, pivot, fromIndex, m);
        } else {
            return recursive(list, pivot, m, toIndex);
        }
    }

    public static void main(String[] args) {
        // Wtf? Why should I check it?
        if (args.length == 1) {
            System.out.println(0);
            return;
        }

        Random random = new Random();
        long x = Long.parseLong(args[0]);
        long[] list = new long[args.length - 1];

        for (int i = 1; i < args.length; i++) {
            list[i - 1] = Integer.parseInt(args[i]);
        }

        if (random.nextBoolean()) {
//            System.out.println("Iterative binary search started");
            System.out.println(iterative(list, x, 0, Math.max(1, args.length - 1)));
        } else {
//            System.out.println("Recursive binary search started");
            System.out.println(recursive(list, x, 0, Math.max(1, args.length - 1)));
        }
    }
}
