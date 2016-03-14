/**
 * Nariman Safiulin (woofilee)
 * File: LinkedQueue.java
 * Created on: Mar 14, 2016
 */

public class LinkedQueue extends AbstractQueue {
    private Node head, tail;

    private class Node {
        Object value;
        Node next, prev;

        Node(Object value, Node next, Node prev) {
            this.value = value;
            this.next = next;
            this.prev = prev;
        }

        Node(Object value, Node next) {
            this(value, next, null);
        }
    }

    @Override
    protected void pushRealization(Object element) {
        if (this.size == 0) {
            this.head = this.tail = new Node(element, null);
        } else {
            this.head = new Node(element, null, this.head);
            this.head.prev.next = this.head;
        }
    }

    @Override
    protected void enqueueRealization(Object element) {
        if (this.size == 0) {
            this.head = this.tail = new Node(element, null);
        } else {
            this.tail = new Node(element, this.tail);
            this.tail.next.prev = this.tail;
        }
    }

    @Override
    protected Object dequeueRealization() {
        Object needle = this.head.value;
        if (this.head.prev != null) {
            this.head.prev.next = null;
        }

        this.head = this.head.prev;
        return needle;
    }

    @Override
    protected Object removeRealization() {
        Object needle = this.tail.value;
        if (this.tail.next != null) {
            this.tail.next.prev = null;
        }

        this.tail = this.tail.next;
        return needle;
    }

    @Override
    public Object[] toArray() {
        Object[] elements = new Object[this.size()];
        Node next = this.head;
        int i = 0;

        while (next != null) {
            elements[i] = next.value;
            next = next.prev;
        }

        return elements;
    }
}
