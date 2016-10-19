/**
 * Nariman Safiulin (woofilee)
 * File: E.java
 * Created on: Dec 20, 2015
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class E {
    static class SegmentTree {
        static class Node implements Comparable<Node> {
            long min;
            boolean isLeaf;
            boolean hasSet;
            boolean hasAdd;
            long set;
            long add;

            Node(long x) {
                min = x;
                isLeaf = false;
                hasSet = false;
                hasAdd = false;
            }

            void setLeaf() {
                isLeaf = true;
            }

            void clearSet() {
                hasSet = false;
            }

            void clearAdd() {
                add = 0;
                hasAdd = false;
            }

            void set(long x) {
                if (isLeaf) {
                    min = x;
                    return;
                }
                if (hasAdd) {
                    clearAdd();
                }

                set = x;
                hasSet = true;
                min = x;
            }

            void add(long x) {
                if (isLeaf) {
                    min += x;
                    return;
                }
                if (hasSet) {
                    set += x;
                    min += x;
                    return;
                }

                add += x;
                hasAdd = true;
                min += x;
            }

            @Override
            public int compareTo(Node o) {
                if (min == o.min) {
                    return 0;
                } else if (min < o.min) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }

        int size;
        Node neutral = new Node(Long.MAX_VALUE);
        Node a[];

        SegmentTree(int n) {
            size = 1;
            while (size < n) {
                size <<= 1;
            }

            a = new Node[size << 1];
            for (int i = size + n - 1; i < (size << 1); i++) {
                a[i] = neutral;
            }
        }

        void build() {
            for (int i = size; i-- > 1; ) {
                a[i] = new Node(((a[i << 1].compareTo(a[(i << 1) + 1]) <= 0) ? a[i << 1] : a[(i << 1) + 1]).min);
            }
        }

        private void push(int v) {
            if (a[v].isLeaf) {
                return;
            }

            if (a[v].hasSet) {
                a[v << 1].set(a[v].set);
                a[(v << 1) + 1].set(a[v].set);
                a[v].clearSet();
            }
            if (a[v].hasAdd) {
                a[v << 1].add(a[v].add);
                a[(v << 1) + 1].add(a[v].add);
                a[v].clearAdd();
            }
        }

        private Node minRecursive(int v, int l, int r, int vl, int vr) {
            if (l > r) {
                return neutral;
            }

            if (l == vl && r == vr) {
                return a[v];
            }

            push(v);

            int t = (vl + vr) >> 1;
            Node lMin = minRecursive(v << 1, l, Math.min(r, t), vl, t);
            Node rMin = minRecursive((v << 1) + 1, Math.max(l, t + 1), r, t + 1, vr);
            return (lMin.compareTo(rMin) <= 0) ? lMin : rMin;
        }

        private void setRecursive(int v, int l, int r, int vl, int vr, long x) {
            if (l > r) {
                return;
            }

            if (l == vl && r == vr) {
                a[v].set(x);
                return;
            }

            push(v);

            int t = (vl + vr) >> 1;
            setRecursive(v << 1, l, Math.min(r, t), vl, t, x);
            setRecursive((v << 1) + 1, Math.max(l, t + 1), r, t + 1, vr, x);

            a[v].min = ((a[v << 1].compareTo(a[(v << 1) + 1]) <= 0) ? a[v << 1] : a[(v << 1) + 1]).min;
        }

        private void addRecursive(int v, int l, int r, int vl, int vr, long x) {
            if (l > r) {
                return;
            }

            if (l == vl && r == vr) {
                a[v].add(x);
                return;
            }

            push(v);

            int t = (vl + vr) >> 1;
            addRecursive(v << 1, l, Math.min(r, t), vl, t, x);
            addRecursive((v << 1) + 1, Math.max(l, t + 1), r, t + 1, vr, x);

            a[v].min = ((a[v << 1].compareTo(a[(v << 1) + 1]) <= 0) ? a[v << 1] : a[(v << 1) + 1]).min;
        }

        Node min(int l, int r) {
            return minRecursive(1, l, r, 1, size);
        }

        void set(int l, int r, long x) {
            setRecursive(1, l, r, 1, size, x);
        }

        void add(int l, int r, long x) {
            addRecursive(1, l, r, 1, size, x);
        }
    }

    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(new File("rmq2.in"));
        PrintWriter pw = new PrintWriter(new File("rmq2.out"));

        int n = sc.nextInt();
        SegmentTree st = new SegmentTree(n);

        for (int i = 0; i < n; i++) {
            st.a[st.size + i] = new SegmentTree.Node(sc.nextLong());
            st.a[st.size + i].setLeaf();
        }

        st.build();

        while (sc.hasNext()) {
            switch (sc.next()) {
                case "set":
                    st.set(sc.nextInt(), sc.nextInt(), sc.nextLong());
                    break;
                case "add":
                    st.add(sc.nextInt(), sc.nextInt(), sc.nextLong());
                    break;
                case "min":
                    pw.println(st.min(sc.nextInt(), sc.nextInt()).min);
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
