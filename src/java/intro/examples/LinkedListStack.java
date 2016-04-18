public class LinkedListStack<E> implements Stack<E> {
    class Node {
        E v;
        Node p;
    }

    Node head;

    public void push(E v) {
        Node n = new Node();
        n.v = v;
        n.p = head;
        head = n;
    }

    public E pop() {
        E t = head.v;
        head = head.p;
        return t;
    }

    public static void main(String[] args) throws Exception {
        Stack.test(new LinkedListStack<String>(), new LinkedListStack<Integer>());
    }
}
