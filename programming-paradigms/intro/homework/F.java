/**
* Nariman Safiulin (woofilee)
* File: F.java
* Created on: Nov 19, 2015
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.TreeMap;

public class F {
    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(new File("f.in"));
        PrintWriter pw = new PrintWriter(new File("f.out"));

        TreeMap<String, Integer> dict = new TreeMap<>();
        String s;
        long all = 0, unique = 0;
        String r = sc.nextLine();

        while (r != null) {
            switch (r.charAt(0)) {
                case '+':
                    s = r.substring(2);
                    if (dict.containsKey(s)) {
                        dict.replace(s, dict.get(s) + 1);
                    } else {
                        dict.put(s, 1);
                        unique++;
                    }
                    all++;
                    break;
                case '-':
                    s = r.substring(2);
                    Integer t = dict.get(s);
                    if (dict.containsKey(s)) {
                        if (t == 1) {
                            dict.remove(s);
                            unique--;
                        } else {
                            dict.replace(s, t - 1);
                        }
                        all--;
                    }
                    break;
                case '*':
                    pw.println(dict.getOrDefault(r.substring(2), 0));
                    break;
                case '?':
                    if (r.equals("?")) {
                        pw.println(all);
                    } else {
                        pw.println(unique);
                    }
                    break;
            }
            r = sc.nextLine();
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

        public String nextLine() throws Exception {
            return br.readLine();
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
