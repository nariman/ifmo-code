/**
 * Nariman Safiulin (woofilee)
 * File: I.java
 * Created on: Dec 23, 2015
 */

import java.net.ServerSocket;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;

public class I {
//    static class Guest {
//        int num;
//        int eaten;
//        int from;
//
//        Guest(int n, int f) {
//            num = n;
//            from = f;
//            eaten = 0;
//        }
//    }

    static class SegmentTree {
        static class Node implements Comparable<Node> {
            int num;
            Node left;
            Node right;
            boolean isLeaf;
//            Guest g[];
            int delta;

            Node(int n/*, int m*/, Node l, Node r) {
                num = n;
                left = l;
                right = r;
                isLeaf = false;
//                g = new Guest[m + 1];
                delta = 0;
            }

            void push() {
                if (isLeaf) {
                    return;
                }

                left.subtract(delta);
                right.subtract(delta);
                delta = 0;
            }

            void subtract(int d/*int guest*/) {
                if (isLeaf) {
                    if (num == 0) {
                        return;
                    }
                    num -= d;
                } else {
                    delta += d;
                }
            }

            void setLeaf () {
                isLeaf = true;
            }

            @Override
            public int compareTo(Node o) {
                return (num < o.num) ? -1 : (num > o.num) ? 1 : 0;
            }
        }

        int size;
        Node neutral;
        Node a[];

        SegmentTree(int n) {
            neutral = new Node(0/*, 0*/, null, null);
            neutral.setLeaf();

            size = 1;
            while (size < n + 5) {
                size <<= 1;
            }

            a = new Node[size << 1];
            for (int i = size + n - 1; i < (size << 1); i++) {
                a[i] = neutral;
            }
        }

        void build(/*int m*/) {
            Arrays.sort(a, size, (size << 1) - 1);
            for (int i = size; i --> 1;) {
                a[i] = new Node(0/*, m*/, a[i << 1], a[(i << 1) + 1]);
            }
        }

        private void decreaseRecursive(int v, int l, int r, int vl, int vr/*, Guest g*/) {
            if (l > r) {
                return;
            }

            if (l == vl && r == vr) {
                a[v].subtract(1/*g*/);
                return;
            }

            a[v].push();

            int t = (vl + vr) >> 1;
            decreaseRecursive(v << 1, l, Math.min(r, t), vl, t/*, g*/);
            decreaseRecursive((v << 1) + 1, Math.max(l, t + 1), r, t + 1, vr/*, g*/);
        }

        private void decrease(int from/*, Guest g*/) {
            decreaseRecursive(1, from, size, 1, size/*, g*/);
        }

        Node get(int i) {
            int v = 1;
            i += size - 1;
            int k = size >> 1;
            int m = size + k;
            k >>= 1;

            while (v != i) {
                a[v].push();

                if (i < m) {
                    m -= k;
                    v = v << 1;
                } else {
                    m += k;
                    v = (v << 1) + 1;
                }

                k >>= 1;
            }

            return a[v];
        }

        int eat(int from/*Guest g*/) {
            int l = 1;
            int r = size;
            int m;

            while (l < r - 1) {
                m = (l + r) >> 1;

                if (get(m).num < from) {
                    l = m;
                } else {
                    r = m;
                }
            }

//            System.out.println("BS found at " + r + " - " + get(r).num);
            if (r == size) {
                return 0;
            }

            decrease(r);
            return size - r;
        }
    }

    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(new File("candies.in"));
        PrintWriter pw = new PrintWriter(new File("candies.out"));

        int n = sc.nextInt();
        SegmentTree st = new SegmentTree(n);

        for (int i = 0; i < n; i++) {
            st.a[st.size + i] = new SegmentTree.Node(sc.nextInt()/*, 0*/, null, null);
            st.a[st.size + i].setLeaf();
        }

        int m = sc.nextInt();
//        Guest g[] = new Guest[m + 1];
        st.build(/*m*/);


//        for (int i = 0; i < n; i++) {
//            System.out.print(st.a[st.size + i].num + " ");
//        }
//
//        System.out.println();
//
//        for (int i = 1; i <= n; i++) {
//            System.out.print(st.get(i).num + " ");
//        }
//
//        System.out.println();

        int t;
        for (int i = 0; i < m; i++) {
//            for (int j = 0; j < st.size; j++) {
//                System.out.print(st.a[st.size + j].num + " ");
//            }
//            for (int j = 1; j <= st.size; j++) {
//                System.out.print(st.get(j).num + " ");
//            }

//            g[i] = new Guest(i, sc.nextInt());
            pw.println(st.eat(sc.nextInt()/*g[i]*/));
        }

//        for (int i = 1; i <= st.size; i++) {
////            System.out.print(st.get(i).num + " ");
//            st.get(i);
//        }
//
//        for (int i = 0; i < m; i++) {
//            pw.println(g[i].eaten);
//        }

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
