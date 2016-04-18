import java.io.File;
import java.io.PrintWriter;

public class LinkedStack {
    class Node {
        int v;
        Node p;
    }

    Node head;

    void push(int v) {
        Node n = new Node();
        n.v = v;
        n.p = head;
        head = n;
    }

    int pop() {
        int t = head.v;
        head = head.p;
        return t;
    }

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(new File("stack.in"));
        PrintWriter out = new PrintWriter(new File("stack.out"));

        LinkedStack stack1 = new LinkedStack();
        LinkedStack stack2 = new LinkedStack();

        while (in.hasNext()) {
            String op = in.next();
            if (op.equals("+")) {
                stack1.push(in.nextInt());
            } else if (op.equals("++")) {
                stack2.push(in.nextInt());
            } else if (op.equals("-")) {
                out.println(stack1.pop());
            } else {
                out.println(stack2.pop());
            }
        }

        in.close();
        out.close();
    }    
}
