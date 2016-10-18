/**
 * Nariman Safiulin (woofilee)
 * File: M.java
 * Created on: Jan 06, 2016
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class M {
    static class DisjointSetForest {
        Random random = new Random();
        int[] data;
        int[] trees;
        boolean status;

        DisjointSetForest(int size) {
            size += 2;
            data = new int[size];
            trees = new int[size];
            status = true;
            while (size-- > 0) data[size] = trees[size] = size;
        }

        int find(int v) {
            return (data[v] == v) ? v : (data[v] = find(data[v]));
        }

        boolean unite(int x, int y) {
            if (!status) {
                return false;
            }

            if (x == y) {
                status = false;
                return false;
            } else if (data[x] == x && trees[x] == x) {
                if (data[y] == y && trees[y] == y) { // X and Y is simple (not trees yet) => create an opposites
                    trees[x] = y;
                    trees[y] = x;
                } else { // X is simple and Y is tree => join X to opposite side of Y
                    int t = find(y);
                    data[x] = trees[t];
                }
            } else if (data[y] == y && trees[y] == y) { // X is tree and Y is simple => join Y to opposite side of X
                int t = find(x);
                data[y] = trees[t];
            } else { // X and Y is tree => join X and Y opposites in correct way
                int t1 = find(x), t2 = find(y);

                if (t1 == t2) { // X and Y in same opposites, it's wrong!
                    status = false;
                    return false;
                }
                if (t1 == trees[t2]) { // X and Y in same tree, but opposite (is correct), nothing to do...
                    return true;
                }

                if (random.nextInt() % 2 == 0) {
                    data[t1] = trees[t2];
                    data[trees[t1]] = t2;
                } else {
                    data[t2] = trees[t1];
                    data[trees[t2]] = t1;
                }
            }

            return true;
        }
    }

    static void solve() throws Exception {
        Scanner sc = new Scanner(new File("addedge.in"));
        PrintWriter pw = new PrintWriter(new File("addedge.out"));
        DisjointSetForest dsf = new DisjointSetForest(sc.nextInt());
        int m = sc.nextInt();

        while (m-- > 0) {
            pw.print(dsf.unite(sc.nextInt(), sc.nextInt()) ? 1 : 0);
        }

        sc.close();
        pw.close();
    }

    static void testgen() throws Exception {
        PrintWriter in = new PrintWriter(new File("addedge.in"));
        Random random = new Random();

        System.out.println("Generating test...");
        System.out.println("n: 10 <= n <= 100, m: n / 2 <= m <= n * 2");

        int n = 0, m = 0;
        while (n < 10) n = random.nextInt(100);
        while (m < n >> 1) m = random.nextInt(n << 1);

        System.out.println("Selected: n=" + n + ", m=" + m);

        in.println(n + " " + m);
        while (m-- > 0) {
            in.println((1 + random.nextInt(n)) + " " + (1 + random.nextInt(n)));
        }

        in.close();

        System.out.println("Done!");
    }

    static void ansgen() throws Exception {
        System.out.println("Generating answer...");

        Scanner sc = new Scanner(new File("addedge.in"));
        PrintWriter ans = new PrintWriter(new File("addedge.ans"));

        int n = sc.nextInt(), m = sc.nextInt();

        class Node {
            ArrayList<Node> nei;
            int op;

            Node() {
                nei = new ArrayList<>();
            }

            void addNode(Node v) {
                nei.add(v);
            }
        }

        Node[] nodes = new Node[n + 1];
        for (int i = n + 1; i-- > 1; ) {
            nodes[i] = new Node();
        }

        ArrayList<Node> q = new ArrayList<>();
        int v1, v2;
        final boolean[] b = new boolean[1];
        b[0] = true;

        while (m --> 0) {
            if (!b[0]) {
                ans.print(0);
                continue;
            }

            for (int i = n + 1; i-- > 1; ) {
                nodes[i].op = 0;
            }

            v1 = sc.nextInt();
            v2 = sc.nextInt();

            nodes[v1].addNode(nodes[v2]);
            nodes[v2].addNode(nodes[v1]);

            for (int i = n + 1; i-- > 1; ) {
                if (nodes[i].op == 0) {
                    nodes[i].op = 1;
                    q.add(nodes[i]);

                    while (q.size() > 0) {
                        Node curr = q.remove(0);
                        curr.nei.forEach((Node v) -> {
                            if (v.op == curr.op) {
                                b[0] = false;
                            }
                            if (v.op == 0) {
                                v.op = (curr.op == 1) ? 2 : 1;
                                q.add(v);
                            }
                        });
                    }
                }
            }

            ans.print(b[0] ? 1 : 0);
        }

        ans.close();
        System.out.println("Done!");
    }

    static void check() throws Exception {
        System.out.println("Checking...");

        Scanner out = new Scanner(new File("addedge.out"));
        Scanner ans = new Scanner(new File("addedge.ans"));

        String outS = out.next();
        String ansS = ans.next();

        if (outS.length() != ansS.length()) {
            System.out.println("WRONG ANSWER! LENGTHS ARE NOT EQUAL!");
        } else {
            for (int i = 0; i < ansS.length(); i++) {
                if (outS.charAt(i) != ansS.charAt(i)) {
                    System.out.println("WRONG ANSWER! POSITION: " + i);
                    break;
                }
            }
        }

        System.out.println("Done!");
    }

    public static void main(String args[]) throws Exception {
//        testgen();
//        ansgen();
        solve();
//        check();
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
