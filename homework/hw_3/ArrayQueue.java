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
     * Inv: If doubles, head < tail
     *
     * Pre: newCapacity > 0
     * Post: Doubles a capacity, if required capacity greater than current capacity, otherwise, returns
     *
     * @param   newCapacity     int     Required size of array
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
     * Adds element to the end of the queue
     *
     * Inv: tail == head, if queue is empty
     * Inv: tail > head, if size < (capacity - head)
     * Inv: tail < head, if size >= (capacity - head)
     *
     * Pre: el != null, el is immutable
     * Post: Adds element to the end of the queue (size' = size + 1)
     */
    public void enqueue(Object el) {
        assert el != null;
        ensureCapacity(this.size + 1);
        this.size++;

        this.elements[this.tail] = el;
        this.tail = (this.tail + 1) % this.capacity;
    }

    /**
     * Returns the first element in the queue
     *
     * Pre: Queue must contains at least one element (size > 0)
     * Post: Returns the first element in the queue
     */
    public Object element() {
        assert this.size > 0;
        return this.elements[this.head];
    }

    /**
     * Remove and returns the first element in the queue
     *
     * Inv: tail == head, if queue is empty
     * Inv: tail > head, if size < (capacity - head)
     * Inv: tail < head, if size >= (capacity - head)
     *
     * Pre: Queue must contains at least one element (size > 0)
     * Post: Returns the first element at the queue and removes it from the queue (size' = size - 1)
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
     * Post: Returns a boolean, which indicated, that queue is empty, or not
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
     * Adds element to the start of the queue
     *
     * Inv: tail == head, if queue is empty
     * Inv: tail > head, if size < (capacity - head)
     * Inv: tail < head, if size >= (capacity - head)
     *
     * Pre: el != null, el is immutable
     * Post: Adds element to the start of the queue (size' = size + 1)
     */
    public void push(Object el) {
        assert el != null;
        ensureCapacity(this.size +  1);
        this.size++;

        if (this.head - 1 >= 0) {
            this.elements[--this.head] = el;
        } else {
            this.elements[this.capacity - 1] = el;
            this.head = this.capacity - 1;
        }
    }

    /**
     * Returns the last element in the queue
     *
     * Pre: Queue must contains at least one element (size > 0)
     * Post: Returns the last element in the queue
     */
    public Object peek() {
        assert this.size > 0;
        return this.elements[(this.tail - 1 >= 0) ? this.tail - 1 : this.capacity - 1];
    }

    /**
     * Removes the last element in the queue
     *
     * Inv: tail == head, if queue is empty
     * Inv: tail > head, if size < (capacity - head)
     * Inv: tail < head, if size >= (capacity - head)
     *
     * Pre: Queue must contains at least one element (size > 0)
     * Post: Returns the last element at the queue and removes it from the queue (size' = size - 1)
     */
    public Object remove() {
        assert this.size > 0;
        this.size--;

        this.tail = (this.tail - 1 >= 0) ? this.tail - 1 : this.capacity - 1;
        return this.elements[this.tail];
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
