//package ru.luvas.dlab32;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

/**
 *
 * @author 0xC0deBabe <iam@kostya.sexy>
 */
public class B {

    public static void main(String[] args) {
        new ProblemSolver("destroy") {

            private int n, m;
            private long s;
            private Edge[] graph;
            private Set<Edge> selected, notSelected;
            private int[] p, size;

            @Override
            public void solve() {
                n = scan.nextInt();
                m = scan.nextInt();
                s = scan.nextLong();
                graph = new Edge[m];
                selected = new HashSet<>();
                notSelected = new HashSet<>();
                p = new int[n];
                size = new int[n];
                Arrays.fill(size, 1);
                for(int i = 0; i < n; ++i)
                    p[i] = i;
                for(int i = 0; i < m; ++i)
                    graph[i] = new Edge(i + 1, scan.nextInt() - 1, scan.nextInt() - 1, scan.nextLong());
                Arrays.sort(graph, (a, b) -> -Long.valueOf(a.w).compareTo(b.w));
                for(Edge e : graph)
                    if(get(e.a) != get(e.b)) {
                        selected.add(e);
                        unite(e.a, e.b);
                    }
                for(Edge e : graph)
                    if(!selected.contains(e))
                        notSelected.add(e);
                graph = new Edge[notSelected.size()];
                graph = notSelected.toArray(graph);
                Arrays.sort(graph, (a, b) -> Long.valueOf(a.w).compareTo(b.w));
                selected.clear();
                long sum = 0;
                for(Edge e : graph)
                    if(sum + e.w <= s) {
                        sum += e.w;
                        selected.add(e);
                    }else
                        break;
                graph = new Edge[selected.size()];
                graph = selected.toArray(graph);
                Arrays.sort(graph, (a, b) -> a.id - b.id);
                println(selected.size());
                StringBuilder sb = new StringBuilder();
                for(Edge e : graph)
                    sb.append(e.id).append(" ");
                println(sb.toString().trim());
            }

            private int get(int v) {
                return v == p[v] ? v : (p[v] = get(p[v]));
            }

            private void unite(int a, int b) {
                a = get(a); b = get(b);
                if(a == b)
                    return;
                if(size[a] < size[b]) {
                    int c = a;
                    a = b;
                    b = c;
                }
                p[b] = a;
                size[a] += size[b];
            }

            class Edge {

                private final int id;
                private final int a, b;
                private final long w;

                public Edge(int id, int a, int b, long w) {
                    this.id = id;
                    this.a = a;
                    this.b = b;
                    this.w = w;
                }

            }

        }.run();
    }

    private abstract static class ProblemSolver {

        private final String problem;
        public FastScanner scan;
        public PrintWriter pw;
        private Thread asyncTimer;
        private volatile boolean asyncTimerInterrupted = false;

        public ProblemSolver(String problem) {
            this.problem = problem;
            try {
                if(problem == null) {
                    scan = new FastScanner(System.in);
                    pw = new PrintWriter(System.out);
                }else {
                    scan = new FastScanner(new FileInputStream(problem + ".in"));
                    pw = new PrintWriter(new FileWriter(problem + ".out"));
                }
            }catch(Exception ex) {
                ex.printStackTrace();
            }
        }

        public void run() {
            try {
                solve();
            }finally {
                pw.close();
            }
        }

        public abstract void solve();

        public void generateTest(PrintWriter pw, String testName) {};

        public void test(String testName) {
            test(testName, false);
        }

        public void test(String testName, boolean enableAsyncTimer) {
            try {
                PrintWriter pw = new PrintWriter(new FileWriter(problem + ".in"));
                try {
                    generateTest(pw, testName);
                }finally {
                    pw.close();
                }
                long currentTime = System.currentTimeMillis();
                if(enableAsyncTimer)
                    runAsyncTimer(currentTime);
                solve();
                stopAsyncTimer();
                System.out.println(String.format("%dms passed in total", System.currentTimeMillis() - currentTime));
            }catch(Exception ex) {
                System.out.println("Problem can't be tested on " + testName + "!");
                ex.printStackTrace();
            }
        }

        private void runAsyncTimer(long startTime) {
            asyncTimer = new Thread() {

                @Override
                public void run() {
                    while(!asyncTimerInterrupted) {
                        System.out.println(String.format("%dms passed", System.currentTimeMillis() - startTime));
                        try {
                            Thread.sleep(1000l);
                        }catch(Exception ex) {}
                    }
                    asyncTimerInterrupted = false;
                    asyncTimer = null;
                }

            };
            asyncTimer.setDaemon(true);
            asyncTimer.start();
        }

        private void stopAsyncTimer() {
            if(asyncTimer == null)
                return;
            asyncTimerInterrupted = true;
        }

        public void print(Object s) {
            pw.print(s);
        }

        public void print(String s, Object... args) {
            print(String.format(s, args));
        }

        public void println(Object s) {
            pw.println(s);
        }

        public void println(String s, Object... args) {
            println(String.format(s, args));
        }

    }

    private static class Pair<A, B> {

        public A a;
        public B b;

        public Pair(A a, B b) {
            this.a = a;
            this.b = b;
        }

    }

    private static class FastScanner {

        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public FastScanner(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

        public long nextLong() {
            return Long.parseLong(next());
        }

        public double nextDouble() {
            return Double.parseDouble(next());
        }

    }

}
