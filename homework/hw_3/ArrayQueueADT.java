/**
 * Nariman Safiulin (woofilee)
 * File: ArrayQueueADT.java
 * Created on: Feb 29, 2016
 */

public class ArrayQueueADT {
    private static final int START_CAPACITY = 1 << 10;

    private int capacity;
    private int size;
    private int head, tail;
    private Object[] elements;

    public ArrayQueueADT() {
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
     *
     * @param   newCapacity     int     Required size of array
     */
    private static void ensureCapacity(ArrayQueueADT queue, int newCapacity) {
        if (newCapacity < queue.capacity) {
            return;
        }

        int to = (queue.tail < queue.head) ? queue.capacity : queue.tail;
        queue.capacity <<= 1;
        Object[] grow = new Object[queue.capacity];

        System.arraycopy(queue.elements, queue.head, grow, 0, to - queue.head);
        if (queue.tail < queue.head) {
            System.arraycopy(queue.elements, 0, grow, to - queue.head, queue.tail);
        }

        queue.elements = grow;
        queue.head = 0;
        queue.tail = queue.size;
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
    public static void enqueue(ArrayQueueADT queue, Object el) {
        ensureCapacity(queue, queue.size + 1);
        queue.elements[queue.tail] = el;
        queue.tail = (queue.tail + 1) % queue.capacity;
        queue.size++;
    }

    /**
     * Returns first element at the queue
     *
     * Pre: Queue must contains at least one object (size > 0)
     * Post: Returns first element at the queue
     * Post: elements[head] != null
     */
    public static Object element(ArrayQueueADT queue) {
        assert queue.size > 0;
        return queue.elements[queue.head];
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
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.size > 0;
        queue.size--;
        int from = queue.head;
        queue.head = (queue.head + 1) % queue.capacity;
        return queue.elements[from];
    }

    /**
     * Returns current size of the queue
     *
     * Pre: -
     * Post: Returns current size of the queue
     */
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    /**
     * Checks if the queue is empty
     *
     * Pre: -
     * Post: Returns a boolean, which indicated, that queue is empty, or not
     */
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    /**
     * Removes all elements from the queue
     *
     * Pre: -
     * Post: Resets the queue to the initial state
     */
    public static void clear(ArrayQueueADT queue) {
        queue.capacity = START_CAPACITY;
        queue.size = 0;
        queue.head = 0;
        queue.tail = 0;
        queue.elements = new Object[START_CAPACITY];
    }

    /**
     * Pre: -
     * Post: Returns a array with elements of queue, from first element to last
     */
    public static Object[] toArray(ArrayQueueADT queue) {
        int to = (queue.tail < queue.head) ? queue.capacity : queue.tail;
        Object[] arr = new Object[queue.size];

        System.arraycopy(queue.elements, queue.head, arr, 0, to - queue.head);
        if (queue.tail < queue.head) {
            System.arraycopy(queue.elements, 0, arr, to - queue.head, queue.tail);
        }

        return arr;
    }
}
