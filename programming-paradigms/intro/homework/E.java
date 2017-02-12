/**
* Nariman Safiulin (woofilee)
* File: E.java
* Created on: Nov 18, 2015
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.*;

public class E {
    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(new File("e.in"), ".,~!@#№$;%^:&?*()_|+=/\\<>{}[] \t\n\r\f");
        PrintWriter pw = new PrintWriter(new File("e.out"));

        TreeMap<String, Integer> dict = new TreeMap<>();
        String s;

        while (sc.hasNext()) {
            s = sc.next().toLowerCase();
            if (dict.containsKey(s)) {
                dict.replace(s, dict.get(s) + 1);
            } else {
                dict.put(s, 1);
            }
        }

        ArrayList<Pair> sortedDict = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : dict.entrySet()) {
            sortedDict.add(new Pair(entry.getKey(), entry.getValue()));
        }

        sortedDict.sort((o1, o2) -> {
            if (o1.val == o2.val) {
                return o1.key.compareTo(o2.key);
            }
            return o2.val - o1.val;
        });

        sortedDict.forEach((el) -> {
            pw.println(el.val + " " + el.key);
        });

        sc.close();
        pw.close();
    }

    static class Pair {
        String key;
        int val;

        Pair(String k, int v) {
            key = k;
            val = v;
        }
    }

    static class Tokenizer {
        ArrayList<String> tokens = new ArrayList<>();
        int len;
        int cur = 0;

        Tokenizer(String str, String delimiters) {
            StringBuilder sb = new StringBuilder();
            final int l = str.length();
            char c;

            for (int i = 0; i < l; i++) {
                c = str.charAt(i);
                if (delimiters.indexOf(c) < 0) {
                    sb.append(c);
                } else {
                    if (sb.length() > 0) {
                        tokens.add(sb.toString());
                        sb = new StringBuilder();
                    }
                }
            }

            if (sb.length() > 0) {
                tokens.add(sb.toString());
                sb = new StringBuilder();
            }
            len = tokens.size();
        }

        boolean hasMoreTokens() {
            return cur < len;
        }

        String nextToken() {
            if (hasMoreTokens()) {
                return tokens.get(cur++);
            }
            return null;
        }
    }

    static class Scanner {
        BufferedReader br;
        Tokenizer t;
        String delimiters;

        Scanner(File file, String delimiters) throws Exception {
            br = new BufferedReader(new FileReader(file));
            t = new Tokenizer("", delimiters);
            this.delimiters = delimiters;
        }

        boolean hasNext() throws Exception {
            while (!t.hasMoreTokens()) {
                String line = br.readLine();
                if (line == null) {
                    return false;
                }
                t = new Tokenizer(line, delimiters);
            }
            return true;
        }

        String next() throws Exception {
            if (!hasNext()) {
                return null;
            }
            return t.nextToken();
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
