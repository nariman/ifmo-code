import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Random;
import java.util.StringTokenizer;

/**
 * Created by woofi on 20.12.2015.
 */
public class G {
    private static final Random rand = new Random();

    static class ImplicitTreap {
        static class PairNode {
            Node left;
            Node right;

            PairNode(Node l, Node r) {
                left = l;
                right = r;
            }
        }

        static class Node {
            long len; // river
            long square; // river

            int y;
            int size;
            Node left;
            Node right;

            Node(long len, int y, Node left, Node right) {
                this.len = len; // river
                this.y = y;
                this.left = left;
                this.right = right;
                fix();
            }

            private int size(Node node) {
                return (node != null) ? node.size : 0;
            }

            // river
            private long square(Node node) {
                return (node != null) ? node.square : 0;
            }

            private void fix() {
                square = square(left) + square(right) + len * len; // river
                size = size(left) + size(right) + 1;
            }
        }

        Node root;
        int size;

        ImplicitTreap() {
            root = null;
            size = 0;
        }

        private int size(Node node) {
            return (node != null) ? node.size : 0;
        }

        private Node merge(Node l, Node r) {
            if (l == null) {
                return r;
            }
            if (r == null) {
                return l;
            }

            if (l.y > r.y) {
                return new Node(l.len, l.y, l.left, merge(l.right, r));
            } else {
                return new Node(r.len, r.y, merge(l, r.left), r.right);
            }
        }

        private PairNode split(Node pivot, int x) {
            PairNode pn = new PairNode(null, null);
            PairNode t = new PairNode(null, null);
            int pivotIndex = size(pivot.left) + 1;

            if (pivotIndex <= x) {
                if (pivot.right == null) {
                    pn.right = null;
                } else {
                    t = split(pivot.right, x - pivotIndex);
                    pn.right = t.right;
                }
                pn.left = new Node(pivot.len, pivot.y, pivot.left, t.left);
            } else {
                if (pivot.left == null) {
                    pn.left = null;
                } else {
                    t = split(pivot.left, x);
                    pn.left = t.left;
                }
                pn.right = new Node(pivot.len, pivot.y, t.right, pivot.right);
            }

            return pn;
        }

        void add(int x, long len) {
            if (root == null) {
                root = new Node(len, rand.nextInt(), null, null);
            } else {
                PairNode pn = split(root, x);
                root = merge(merge(pn.left, new Node(len, rand.nextInt(), null, null)), pn.right);
            }

            //System.out.println(x + " " + len + " root: " + root.len + " " + root.y + " " + root.size);
            size++;
        }

        long remove(int x) {
            long len = 0;

            if (root != null) {
                Node t;
                PairNode pn = split(root, x);
                t = pn.left;
                pn = split(pn.right, 1);
                root = merge(t, pn.right);
                len = pn.left.len;
            }

            size--;
            return len; // self
        }

        /* ------ RIVER ------ */

        void separate(int x) {
            long len = remove(x);
            add(x, len >> 1);
            add(x + 1, (len >> 1) + len % 2);
        }

        void bankrupt(int x) {
            if (x == 0) {
                long lenFirst = remove(0);
                long lenSecond = remove(0);
                add(0, lenFirst + lenSecond);
            } else if (x == size - 1) {
                long lenLast = remove(size - 1);
                long lenPenult = remove(size - 1);
                add(size, lenPenult + lenLast);
            } else {
                long lenBefore = remove(x - 1);
                long len = remove(x - 1);
                long lenAfter = remove(x - 1);
                add(x - 1, lenBefore + (len >> 1));
                add(x, (len >> 1) + len % 2 + lenAfter);
            }
        }

        long getAnswer() {
            return root.square;
        }
    }

    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(new File("river.in"));
        PrintWriter pw = new PrintWriter(new File("river.out"));
        ImplicitTreap it = new ImplicitTreap();

        int n = sc.nextInt();
        sc.nextInt();
        for (int i = 0; i < n; i++) {
            it.add(i, sc.nextLong());
        }
        pw.println(it.getAnswer());

        int m = sc.nextInt();
        while (m --> 0) {
            switch (sc.nextInt()) {
                case 1:
                    it.bankrupt(sc.nextInt() - 1);
                    pw.println(it.getAnswer());
                    break;
                case 2:
                    it.separate(sc.nextInt() - 1);
                    pw.println(it.getAnswer());
                    break;
            }
        }

        sc.close();
        pw.close();
    }

    static class Scanner {
        BufferedReader br;
        StringTokenizer t;

        Scanner(File file) throws Exception {
            br = new BufferedReader(new FileReader(file));
            t = new StringTokenizer("");
        }

        boolean hasNext() throws Exception {
            while (!t.hasMoreTokens()) {
                String line = br.readLine();
                if (line == null)
                    return false;
                t = new StringTokenizer(line);
            }
            return true;
        }

        String next() throws Exception {
            if (hasNext()) {
                return t.nextToken();
            }
            return null;
        }

        int nextInt() throws Exception {
            return Integer.parseInt(next());
        }

        long nextLong() throws Exception {
            return Long.parseLong(next());
        }

        double nextDouble() throws Exception {
            return Double.parseDouble(next());
        }

        void close() throws Exception {
            br.close();
        }
    }
}
