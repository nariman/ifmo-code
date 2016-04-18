import java.io.File;
import java.io.PrintWriter;

public interface Stack<E> {
    void push(E v);
    E pop();

    static void test(Stack<String> stack1, Stack<Integer> stack2) throws Exception {
        Scanner in = new Scanner(new File("stack2.in"));
        PrintWriter out = new PrintWriter(new File("stack2.out"));

        while (in.hasNext()) {
            switch (in.next()) {
                case "+":
                    stack1.push(in.next());
                    break;
                case "++":
                    stack2.push(in.nextInt());
                    break;
                case "-":
                    out.println(stack1.pop().toUpperCase());
                    break;
                default:
                    out.println(stack2.pop().intValue());
                    break;
            }
        }

        System.out.println(stack1);
        System.out.println(stack2);

        in.close();
        out.close();
    }
}