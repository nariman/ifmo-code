/**
 * Nariman Safiulin (woofilee)
 * File: D.java
 * Created on: Dec 19, 2015
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class D {
    static class SegmentTree {
        static class MatrixNode {
            int x11, x12, x21, x22;

            MatrixNode(int x11, int x12, int x21, int x22) {
                this.x11 = x11;
                this.x12 = x12;
                this.x21 = x21;
                this.x22 = x22;
            }
        }

        int size;
        int r;
        MatrixNode a[];
        MatrixNode neutral = new MatrixNode(1, 0, 0, 1);

        SegmentTree(int n, int r) {
            this.r = r;

            size = 1;
            while (size < n) {
                size <<= 1;
            }

            a = new MatrixNode[size << 1];
            for (int i = size + n - 1; i < (size << 1); i++) {
                a[i] = neutral;
            }
        }

        MatrixNode matrixMultiply(MatrixNode a, MatrixNode b) {
            return new MatrixNode(
                    (a.x11 * b.x11 + a.x12 * b.x21) % r,
                    (a.x11 * b.x12 + a.x12 * b.x22) % r,
                    (a.x21 * b.x11 + a.x22 * b.x21) % r,
                    (a.x21 * b.x12 + a.x22 * b.x22) % r
            );
        }

        void build() {
            for (int i = size; i-- > 1; ) {
                a[i] = matrixMultiply(a[i << 1], a[(i << 1) + 1]);
            }
        }

        private MatrixNode multiplyRecursive(int v, int l, int r, int vl, int vr) {
            if (l > r) {
                return neutral;
            }
            if (l == vl && r == vr) {
                return a[v];
            }
            int t = (vl + vr) >> 1;
            return matrixMultiply(multiplyRecursive(v << 1, l, Math.min(r, t), vl, t),
                    multiplyRecursive((v << 1) + 1, Math.max(l, t + 1), r, t + 1, vr));
        }

        MatrixNode multiply(int l, int r) {
            return multiplyRecursive(1, l, r, 1, size);
        }
    }

    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(new File("crypto.in"));
        PrintWriter pw = new PrintWriter(new File("crypto.out"));

        int r = sc.nextInt();
        int n = sc.nextInt();
        int m = sc.nextInt();
        SegmentTree st = new SegmentTree(n, r);

        for (int i = 0; i < n; i++) {
            st.a[st.size + i] = new SegmentTree.MatrixNode(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextInt());
        }

        st.build();

        SegmentTree.MatrixNode t;
        for (int i = m; i --> 0;) {
            t = st.multiply(sc.nextInt(), sc.nextInt());
            pw.println(t.x11 + " " + t.x12);
            pw.println(t.x21 + " " + t.x22);
            pw.println();
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

        void close() throws Exception {
            br.close();
        }
    }
}
