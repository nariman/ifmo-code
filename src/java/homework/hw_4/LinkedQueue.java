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

    public Object element() {
        return this.head.value;
    }

    public Object peek() {
        return this.tail.value;
    }

    @Override
    protected void dequeueRealization() {
        if (this.head.prev != null) {
            this.head.prev.next = null;
        }
        this.head = this.head.prev;
    }

    @Override
    protected void removeRealization() {
        if (this.tail.next != null) {
            this.tail.next.prev = null;
        }
        this.tail = this.tail.next;
    }

    public AbstractQueue getNewInstance() {
        return new LinkedQueue();
    }

//    @Override
//    public Object[] toArray() {
//        Node next = this.head;
//        int i = 0;
//
//        while (next != null) {
//            elements[i] = next.value;
//            next = next.prev;
//        }
//
//        return elements;
//    }
}
