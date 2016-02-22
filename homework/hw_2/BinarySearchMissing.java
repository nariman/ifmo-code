import java.util.Random;

/**
 * Created by woofi on 21.02.2016.
 */
public class BinarySearchMissing {

    /**
     * Finds the minimum index `i` of element, that list[i] == `x` in `list`, in `list`, sorted in ascending,
     * or, returns the index ( -`i` - 1 ), where element can be inserted, if element not found
     *
     * Inv: `l` less or equal, than minimum index `i` of element, that list[i] == x in `list`
     * Inv: `r` bigger, than minimum index `i` of element, that list[i] == x in `list`
     *
     * @param   list        long[]  array with long integers
     * @param   key         long    needed element
     * @param   fromIndex   int     left bound of search area, including element, l >= 0                        (Pre)
     * @param   toIndex     int     right bound of area, excluding element, r >= 0, r >= l                      (Pre)
     * @return              int     minimum index `i` of element, that list[i] <= x in `list`,                  (Post)
     *                              or index ( -`i` - 1 ), where element can be inserted, if element not found
     */
    private static int iterative(long[] list, long key, int fromIndex, int toIndex) {
        if (fromIndex == toIndex) {
            return -fromIndex - 1;
        }

        int m, last = toIndex;

        while (toIndex - fromIndex > 1) {
            m = fromIndex + (toIndex - fromIndex) / 2;

            if (list[m] <= key) {
                toIndex = m;
            } else {
                fromIndex = m;
            }

        }

        if (list[fromIndex] < key) {
            return -fromIndex - 1;
        } else if (list[fromIndex] == key) {
            return fromIndex;
        } else if (toIndex < last && list[toIndex] == key){
            return toIndex;
        } else {
            return -toIndex - 1;
        }
    }

    /**
     * Finds the minimum index `i` of element, that list[i] == `x` in `list`, in `list`, sorted in ascending,
     * or, returns the index ( -`i` - 1 ), where element can be inserted, if element not found
     *
     * Inv: `l` less, than minimum index `i` of element, that list[i] <= x in `list`
     * Inv: `r` equal or bigger, than minimum index `i` of element, that list[i] <= x in `list`
     *
     * @param   list        long[]  array with long integers
     * @param   key         long    needed element
     * @param   fromIndex   int     left bound of search area, including element, l >= 0                        (Pre)
     * @param   toIndex     int     right bound of area, excluding element, r >= 0, r >= l                      (Pre)
     * @return              int     minimum index `i` of element, that list[i] <= x in `list`,                  (Post)
     *                              or index ( -`i` - 1 ), where element can be inserted, if element not found
     */
    private static int recursive(long list[], long key, int fromIndex, int toIndex) {
//        System.out.println(fromIndex + " " + toIndex);

        if (fromIndex == toIndex) {
            return -fromIndex - 1;
        }

        if (toIndex - fromIndex < 2) {
            if (list[fromIndex] < key) {
                return -fromIndex - 1;
            } else if (list[fromIndex] == key) {
                return fromIndex;
            } else {
                try {
                    if (list[toIndex] == key) {
                        return toIndex;
                    } else {
                        return -toIndex - 1;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                    return -toIndex - 1;
                }
            }
        }

        int m = fromIndex + (toIndex - fromIndex) / 2;

//        System.out.println(fromIndex + " " + m + " (" + list[m] + ") " + toIndex);

        if (list[m] <= key) {
            return recursive(list, key, fromIndex, m);
        } else {
            return recursive(list, key, m, toIndex);
        }
    }

    public static void main(String[] args) {
        Random random = new Random();
        long key = Long.parseLong(args[0]);
        long[] list = new long[args.length - 1];

        for (int i = 1; i < args.length; i++) {
            list[i - 1] = Integer.parseInt(args[i]);
        }

        if (random.nextBoolean()) {
//            System.out.println("Iterative binary search started");
            System.out.println(iterative(list, key, 0, Math.max(0, args.length - 1)));
        } else {
//            System.out.println("Recursive binary search started");
            System.out.println(recursive(list, key, 0, Math.max(0, args.length - 1)));
        }
    }
}
