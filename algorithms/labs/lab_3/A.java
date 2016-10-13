import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

/**
 * Created by woofi on 19.12.2015.
 */
public class A {
    static class SegmentTree {
        int size;
        int a[];

        void build(int[] source, int n) {
            size = 1;
            while (size < n) {
                size <<= 1;
            }
            //System.out.println("Int = " + n + ". Size = " + size + ". 2Size = " + (size << 1));

            a = new int[size << 1];
            for (int i = size; i < (size << 1); i++) {
                a[i] = Integer.MAX_VALUE;
            }
            System.arraycopy(source, 0, a, size, n);

            for (int i = size; i-- > 0; ) {
                a[i] = Math.min(a[i << 1], a[(i << 1) + 1]);
            }

            /*
            for (int i = 0; i < size << 1; i++) {
                System.out.print(a[i] + " ");
            }
            System.out.println("-");
            */
        }

        private int minRecursive(int v, int l, int r, int vl, int vr) {
            //System.out.printf("%d %d %d %d %d\n", v, l, r, vl, vr);
            if (l > r) {
                return Integer.MAX_VALUE;
            }
            if (l == vl && r == vr) {
                return a[v];
            }
            int t = (vl + vr) >> 1;
            return Math.min(minRecursive(v << 1, l, Math.min(r, t), vl, t),
                    minRecursive((v << 1) + 1, Math.max(l, t + 1), r, t + 1, vr));
        }

        int min(int l, int r) {
            return minRecursive(1, l, r, 1, size);
        }

        void update(int i, int x) {
            i += size - 1;
            a[i] = x;
            while ((i >>= 1) != 0) {
                a[i] = Math.min(a[i << 1], a[(i << 1) + 1]);
            }

            /*
            for (i = 0; i < size << 1; i++) {
                System.out.print(a[i] + " ");
            }
            System.out.println("-");
            */
        }
    }

    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(new File("rmq.in"));
        PrintWriter pw = new PrintWriter(new File("rmq.out"));

        int n = sc.nextInt();
        int a[] = new int[n + 1];
        SegmentTree st = new SegmentTree();

        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }

        st.build(a, n);

        int t1, t2;
        while (sc.hasNext()) {
            switch (sc.next()) {
                case "set":
                    t1 = sc.nextInt();
                    t2 = sc.nextInt();
                    st.update(t1, t2);
                    break;
                case "min":
                    t1 = sc.nextInt();
                    t2 = sc.nextInt();
                    pw.println(st.min(t1, t2));
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