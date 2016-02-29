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

    /**
     * Checks, that current capacity of array is enough to append a new element.
     * Otherwise, doubles a capacity of array, and copies existing data in order from zero-cell of array.
     *
     * Pre: newCapacity > 0
     * Post: Doubles a capacity, if required capacity greater than current capacity, otherwise, returns
     */
    private void ensureCapacity(int newCapacity) {
        if (newCapacity < this.capacity) {
            return;
        }

        int to = (this.tail < this.head) ? this.capacity : this.tail;
        this.capacity <<= 1;
        Object[] grow = new Object[this.capacity];

        System.arraycopy(this.elements, this.head, grow, 0, to - this.head);
        if (this.tail < this.head) {
            System.arraycopy(this.elements, 0, grow, to - this.head, this.tail);
        }

        this.elements = grow;
        this.head = 0;
        this.tail = this.size;
    }

    /**
     * Adds element to the queue
     *
     * Inv: tail == head, if queue is empty
     * Inv: tail > head, if size < (capacity - head)
     * Inv: tail < head, if size >= (capacity - head)
     *
     * Pre: el != null
     * Post: Adds element to the tail of queue (size -> size + 1), elements[tail] != null
     */
    public void enqueue(Object el) {
        ensureCapacity(this.size + 1);
        this.elements[this.tail] = el;
        this.tail = (this.tail + 1) % this.capacity;
        this.size++;
    }

    /**
     * Returns first element at the queue
     *
     * Pre: Queue must contains at least one object (size > 0)
     * Post: Returns first element at the queue
     * Post: elements[head] != null
     */
    public Object element() {
        assert this.size > 0;
        return this.elements[this.head];
    }

    /**
     * Remove and returns first element in the queue
     *
     * Inv: tail == head, if queue is empty
     * Inv: tail > head, if size < (capacity - head)
     * Inv: tail < head, if size >= (capacity - head)
     *
     * Pre: Queue must contains at least one object (size > 0)
     * Post: Returns object on the head of queue and removes it from the queue (size -> size - 1)
     * Post: elements[head] != null
     */
    public Object dequeue() {
        assert this.size > 0;
        this.size--;
        int from = this.head;
        this.head = (this.head + 1) % this.capacity;
        return this.elements[from];
    }

    /**
     * Returns current size of the queue
     *
     * Pre: -
     * Post: Returns current size of the queue
     */
    public int size() {
        return this.size;
    }

    /**
     * Checks if the queue is empty
     *
     * Pre: -
     * Post: Returns a value, which indicated, that queue is empty, or not
     */
    public boolean isEmpty() {
        return this.size == 0;
    }

    /**
     * Removes all elements from the queue
     *
     * Pre: -
     * Post: Resets the queue to the initial state (size = 0)
     */
    public void clear() {
        this.capacity = START_CAPACITY;
        this.size = 0;
        this.head = 0;
        this.tail = 0;
        this.elements = new Object[START_CAPACITY];
    }

    /**
     * Pre: -
     * Post: Returns a array with elements of queue, from first element to last
     */
    public Object[] toArray() {
        int to = (this.tail < this.head) ? this.capacity : this.tail;
        Object[] arr = new Object[this.size];

        System.arraycopy(this.elements, this.head, arr, 0, to - this.head);
        if (this.tail < this.head) {
            System.arraycopy(this.elements, 0, arr, to - this.head, this.tail);
        }

        return arr;
    }
}
