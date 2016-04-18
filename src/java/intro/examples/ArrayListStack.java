import java.util.ArrayList;

public class ArrayListStack<E> implements Stack<E> {
    ArrayList<E> stack = new ArrayList<E>();

    public void push(E e) {
        stack.add(e);
    }

    public E pop() {
        return stack.remove(stack.size() - 1);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (E e : stack) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(e);
        }
        return "ArrayQueue[" + sb.append("]").toString();
    }

    public static void main(String[] args) throws Exception {
        Stack.test(new ArrayListStack<String>(), new ArrayListStack<Integer>());
    }
}