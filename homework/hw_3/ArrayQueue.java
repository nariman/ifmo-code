/**
 * Nariman Safiulin (woofilee)
 * File: ArrayQueue.java
 * Created on: Feb 29, 2016
 */

public class ArrayQueue {
    private static final int START_CAPACITY = 1 << 10;

    private int capacity;
    private int size;
    private int head, tail;
    private Object[] elements;

    public ArrayQueue() {
        this.capacity = START_CAPACITY;
        this.size = 0;
        this.head = 0;
        this.tail = 0;
        this.elements = new Object[START_CAPACITY];
    }

    private void ensureCapacity(int newCapacity) {
        if (newCapacity < this.capacity) {
            return;
        }

        int to = (this.tail < this.head) ? this.capacity : this.tail;
        this.capacity <<= 1;
        Object[] grow = new Object[this.capacity];

        for (int i = this.head, j = 0; i < to; i++, j++) {
            grow[j] = this.elements[i];
        }

        if (this.tail < this.head) {
            for (int i = 0, j = to - this.head; i < this.tail; i++, j++) {
                grow[j] = this.elements[i];
            }
        }

        this.elements = grow;
        this.head = 0;
        this.tail = this.size;
    }

    /**
     * Adds element to the ArrayQueue
     */
    public void enqueue(Object el) {
        assert el != null;
        ensureCapacity(this.size + 1);
        this.elements[this.tail] = el;
        this.tail = (this.tail + 1) % this.capacity;
        this.size++;
    }

    /**
     * Returns the first element in the ArrayQueue
     */
    public Object element() {
        assert this.size > 0;
        return this.elements[this.head];
    }

    /**
     * Remove and returns first element in the ArrayQueue
     */
    public Object dequeue() {
        assert this.size > 0;
        this.size--;
        int from = this.head;
        this.head = (this.head + 1) % this.capacity;
        return this.elements[from];
    }

    /**
     * Returns current size of ArrayQueue
     */
    public int size() {
        return this.size;
    }

    /**
     * Checks if the ArrayQueue is empty
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Removes all elements from the ArrayQueue
     */
    public void clear() {
        this.capacity = START_CAPACITY;
        this.size = 0;
        this.head = 0;
        this.tail = 0;
        this.elements = new Object[START_CAPACITY];
    }
}
