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
     * <p>
     * Pre: newCapacity > 0
     * Post: Doubles a capacity, if required capacity greater than current capacity, otherwise, returns
     * Inv: If doubles, head < tail
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
     * Pre: -
     * Post: Adds element to the head of the queue
     * Inv: size' = size + 1
     */
    public static void push(ArrayQueueADT queue, Object el) {
        assert el != null;
        ensureCapacity(queue, queue.size + 1);
        queue.size++;

        if (queue.head - 1 >= 0) {
            queue.elements[--queue.head] = el;
        } else {
            queue.elements[queue.capacity - 1] = el;
            queue.head = queue.capacity - 1;
        }
    }

    /**
     * Pre: -
     * Post: Adds element to the tail of the queue
     * Inv: size' = size + 1
     */
    public static void enqueue(ArrayQueueADT queue, Object el) {
        ensureCapacity(queue, queue.size + 1);
        queue.size++;

        queue.elements[queue.tail] = el;
        queue.tail = (queue.tail + 1) % queue.capacity;
    }

    /**
     * Pre: Queue must contains at least one element (size > 0)
     * Post: Returns the head element at the queue and removes it from the queue
     * Inv: size' = size - 1
     */
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.size > 0;
        queue.size--;

        int from = queue.head;
        queue.head = (queue.head + 1) % queue.capacity;
        return queue.elements[from];
    }

    /**
     * Pre: Queue must contains at least one element (size > 0)
     * Post: Returns the tail element at the queue and removes it from the queue
     * Inv: size' = size - 1
     */
    public static Object remove(ArrayQueueADT queue) {
        assert queue.size > 0;
        queue.size--;

        queue.tail = (queue.tail - 1 >= 0) ? queue.tail - 1 : queue.capacity - 1;
        return queue.elements[queue.tail];
    }

    /**
     * Pre: Queue must contains at least one element (size > 0)
     * Post: Returns the head element in the queue, without changes in queue
     */
    public static Object element(ArrayQueueADT queue) {
        assert queue.size > 0;
        return queue.elements[queue.head];
    }

    /**
     * Pre: Queue must contains at least one element (size > 0)
     * Post: Returns the tail element in the queue, without changes in queue
     */
    public static Object peek(ArrayQueueADT queue) {
        assert queue.size > 0;
        return queue.elements[(queue.tail - 1 >= 0) ? queue.tail - 1 : queue.capacity - 1];
    }

    /**
     * Pre: -
     * Post: Returns current size of the queue, without changes in queue
     */
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    /**
     * Pre: -
     * Post: Returns True, if queue is empty, otherwise False, without changes in queue
     */
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    /**
     * Pre: -
     * Post: Removes all elements from the queue
     * Inv: size' = 0
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
     * Post: Returns a array with elements of the queue, from the first element to the last, without changes in queue
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
