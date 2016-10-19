/**
* Nariman Safiulin (woofilee)
* File: C.java
* Created on: Nov 04, 2015
*/

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;

public class C {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("c.in"), args[0]);
        PrintWriter pw = new PrintWriter("c.out");

        long int_sum = 0;
        double double_sum = 0;

        while (sc.hasNext()) {
            if (sc.hasNextInt()) {
                int_sum += sc.nextInt();
            } else if (sc.hasNextDouble()) {
                double_sum += sc.nextDouble();
            } else {
                sc.skip();
            }
        }

        pw.println(int_sum);
        pw.println(double_sum);
        sc.close();
        pw.close();
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
        String savedToken;
        String delimiters;
        boolean hasSavedToken = false;

        Scanner(File file, String delimiters) throws Exception {
            boolean arr[] = new boolean[128];
            final int len = delimiters.length();

            for (int i = 0; i < len; i++) {
                arr[(int) delimiters.charAt(i)] = true;
            }
            StringBuilder newDelimeters = new StringBuilder();
            for (int i = 0; i < 128; i++) {
                if (arr[i]) {
                    newDelimeters.append((char) i);
                }
            }

            br = new BufferedReader(new FileReader(file));
            t = new Tokenizer("", newDelimeters.toString());
            this.delimiters = newDelimeters.toString();
        }

        boolean hasNext() throws Exception {
            if (hasSavedToken) {
                return true;
            }

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

            if (hasSavedToken) {
                hasSavedToken = false;
                return savedToken;
            }
            return t.nextToken();
        }

        void skip() throws Exception {
            next();
        }

        private String nextToken() throws Exception {
            savedToken = next();
            hasSavedToken = true;
            return savedToken;
        }

        boolean hasNextInt() throws Exception {
            if (!hasNext()) {
                return false;
            }
            try {
                Integer.parseInt(nextToken());
                return true;
            } catch (Exception e) {
                return false;
            }
        }

        boolean hasNextDouble() throws Exception {
            if (!hasNext()) {
                return false;
            }
            try {
                Double.parseDouble(nextToken());
                return true;
            } catch (Exception e) {
                return false;
            }
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
