import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Random;
import java.util.StringTokenizer;

public class J {
    public static void main(String args[]) throws Exception {
        Scanner sc = new Scanner(new File("atoms.in"));
//        PrintWriter sc = new PrintWriter(new File("atoms.in"));
        PrintWriter pw = new PrintWriter(new File("atoms.out"));

        sc.close();
        pw.close();
    }

    static void testGen() {
        Random random = new Random();

        int n = 0;
        while (n < 5) n = random.nextInt(500);

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