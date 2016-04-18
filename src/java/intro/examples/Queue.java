import java.io.*;
import java.util.*;

public class Queue {
    static <E> E pop(List<E> queue) {
        return queue.remove(0);
    }

    public static void main(String[] args) throws Exception {
        Scanner in = new Scanner(new File("queue.in"));
        PrintWriter out = new PrintWriter(new File("queue.out"));

        List<String> queue1 = new ArrayList<String>();
        List<Integer> queue2 = new LinkedList<Integer>();

        while (in.hasNext()) {
            switch (in.next()) {
                case "+":
                    queue1.add(in.next());
                    break;
                case "++":
                    queue2.add(in.nextInt());
                    break;
                case "-":
                    out.println(pop(queue1).toUpperCase());
                    break;
                default:
                    out.println(pop(queue2).intValue());
                    break;
            }
        }
        System.out.println(queue1);

        in.close();
        out.close();
    }    
}