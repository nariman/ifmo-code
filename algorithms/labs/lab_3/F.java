import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * Created by woofi on 20.12.2015.
 */
public class F {
    static class SegmentTree {
        static class Node {
            boolean isFree;
            boolean isLeaf;

            Node() {
                isFree = true;
                isLeaf = false;
            }

            void setLeaf() {
                isLeaf = true;
            }

            void setFree() {
                isFree = true;
            }

            void setBusy() {
                isFree = false;
            }
        }

        int size;
        Node neutral = new Node();
        Node a[];

        SegmentTree(int n) {
            neutral.setLeaf();
            neutral.setBusy();

            size = 1;
            while (size < n) {
                size <<= 1;
            }

            a = new Node[size << 1];

            for (int i = size; i < size + n; i++) {
                a[i] = new Node();
                a[i].setLeaf();
            }
            for (int i = size + n; i < (size << 1); i++) {
                a[i] = neutral;
            }

            for (int i = size; i-- > 1; ) {
                a[i] = new Node();
                if (!(a[i << 1].isFree || a[(i << 1) + 1].isFree))
                {
                    a[i].setBusy();
                }
            }
        }

        int enter(int place) {
            int current = size + place - 1;

            if (a[current].isFree) {
                a[current].setBusy();
                return place;
            }

            int from = size + place - 1;
            int ans = 1 - size;

            while ((current >>= 1) != 0) {
                if ((current << 1) + 1 != from && a[(current << 1) + 1].isFree) {
                    current = (current << 1) + 1;
                    break;
                }
                from = current;
            }
            if (current == 0) {
                current = 1;
            }

            while (!a[current].isLeaf) {
                if (a[current << 1].isFree) {
                    current <<= 1;
                } else {
                    current = (current << 1) + 1;
                }
            }
            a[current].setBusy();
            ans += current;

            while ((current >>= 1) != 0) {
                if (a[current << 1].isFree || a[(current << 1) + 1].isFree) {
                    a[current].setFree();
                } else {
                    a[current].setBusy();
                }
            }

            return ans;
        }

        void exit(int place) {
            int current = size + place - 1;
            a[current].setFree();

            while ((current >>= 1) != 0) {
                a[current].setFree();
            }
        }
    }

    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(new File("parking.in"));
        PrintWriter pw = new PrintWriter(new File("parking.out"));

        SegmentTree st = new SegmentTree(sc.nextInt());

        for (int i = sc.nextInt(); i --> 0;) {
            switch (sc.next()) {
                case "enter":
                    pw.println(st.enter(sc.nextInt()));
                    break;
                case "exit":
                    st.exit(sc.nextInt());
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

        double nextDouble() throws Exception {
            return Double.parseDouble(next());
        }

        void close() throws Exception {
            br.close();
        }
    }
}