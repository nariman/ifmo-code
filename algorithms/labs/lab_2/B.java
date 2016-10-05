import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by woofi on 14.11.2015.
 */

public class B {
    static class Stack {
        ArrayList<Character> stack = new ArrayList<>();

        int size() {
            return stack.size();
        }

        void push(Character c) {
            stack.add(c);
        }

        Character pop() {
            return stack.remove(stack.size() - 1);
        }

        Character last() {
            return stack.get(stack.size() - 1);
        }
    }

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(new File("brackets.in"));
        PrintWriter pw = new PrintWriter("brackets.out");

        String sq = sc.next();

        Stack stack = new Stack();
        int len = sq.length();
        if (len > 0) {
            stack.push(sq.charAt(0));
        }
        int i = 1;

        while (i < len) {
            Character c = sq.charAt(i);
            if (stack.size() == 0) {
                stack.push(sq.charAt(i));
            } else {
                Character t = stack.last();
                if (t == '[' && c == ']' || t == '(' && c == ')' || t == '{' && c == '}') {
                    stack.pop();
                } else {
                    stack.push(c);
                }
            }
            i++;
        }

        if (stack.size() > 0) {
            pw.print("NO");
        } else {
            pw.print("YES");
        }
        pw.close();
        sc.close();
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