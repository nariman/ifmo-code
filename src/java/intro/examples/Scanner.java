import java.io.*;
import java.util.*;

class Scanner {
    BufferedReader br;
    StringTokenizer t = new StringTokenizer("");

    Scanner(File file) throws Exception {
        br = new BufferedReader(new FileReader(file));
        t = new StringTokenizer("");
    }

    boolean hasNext() throws Exception {
        while (!t.hasMoreTokens()) {
            String line = br.readLine();
            if (line == null) {
                return false;
            }
            t = new StringTokenizer(line);
        }
        return t.hasMoreTokens();
    }

    int nextInt() throws Exception {
        return Integer.parseInt(next());
    }

    String next() throws Exception {
        if (hasNext()) {
            return t.nextToken();
        } else {
            return null;
        }
    }

    void close() throws Exception {
        br.close();
    }
}
