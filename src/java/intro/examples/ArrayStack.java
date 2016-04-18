import java.io.File;
import java.io.PrintWriter;

public class ArrayStack {
    int[] stack;
    int top;

    ArrayStack() {
        stack = new int[10];
    }


    ArrayStack(int capacity) {
        stack = new int[capacity];
        //top = 0;
    }

    void push() {
        push(0);
    }

    void push(int v) {
        if (top >= stack.length) {
            int[] newStack = new int[stack.length * 2];
            for (int i = 0; i < top; i++) {
                newStack[i] = stack[i];
            }
            stack = newStack;
        }
        stack[top++] = v;
    }

    int pop() {
        return stack[--top];
    }

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(new File("stack.in"));
        PrintWriter out = new PrintWriter(new File("stack.out"));

        ArrayStack stack1 = new ArrayStack(10);
        ArrayStack stack2 = new ArrayStack(10);

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
