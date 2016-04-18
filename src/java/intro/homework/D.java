/**
 * Created by WooFi on 04.11.2015.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class D {

    static class Node {
        int key;
        int len;
        Node prev;
        int min;
        int max;

        Node() {
            min = Integer.MAX_VALUE;
            max = Integer.MIN_VALUE;
        }

        Node(int key, Node prev) {
            this.key = key;
            this.prev = prev;

            // проверка на null - по сути костыль, что мы "наследуемся" от нулевой версии
            len = prev.len + 1;
            min = Math.min(prev.min, key);
            max = Math.max(prev.max, key);
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("d.in"));
        PrintWriter pw = new PrintWriter("d.out");

        ArrayList<Node> nodes = new ArrayList<Node>();
        nodes.add(new Node()); // Типа нулевой элемент (версия), чтобы хоть чтобы было изначально, типа костыль

        String cmd;
        int version, key;

        while (sc.hasNext()) {
            cmd = sc.next();
            version = sc.nextInt();
            //System.out.println(cmd + " " + version);
            Node temp;
            switch (cmd) {
                case "+":
                    key = sc.nextInt();
                    nodes.add(new Node(key, nodes.get(version)));
                    pw.println(nodes.get(nodes.size() - 1).len);
                    break;
                case "-":
                    temp = nodes.get(version);
                    nodes.add(temp.prev);
                    pw.println(temp.key);
                    break;
                case "max":
                    temp = nodes.get(version);
                    nodes.add(temp);
                    pw.println(temp.max);
                    break;
                case "min":
                    temp = nodes.get(version);
                    //System.out.println(temp.key + " " + temp.len + " " + temp.prev + " " + temp.min + " " + temp.max);
                    nodes.add(temp);
                    pw.println(temp.min);
                    break;
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
            return t.hasMoreTokens();
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