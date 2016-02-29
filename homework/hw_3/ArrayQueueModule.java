/**
 * Nariman Safiulin (woofilee)
 * File: ArrayQueueModule.java
 * Created on: Feb 29, 2016
 */

public class ArrayQueueModule {
    private static final int START_CAPACITY = 1 << 10;

    private static int capacity = START_CAPACITY;
    private static int size = 0;
    private static int head = 0, tail = 0;
    private static Object[] elements = new Object[START_CAPACITY];

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
    private static void ensureCapacity(int newCapacity) {
        if (newCapacity < capacity) {
            return;
        }

        int to = (tail < head) ? capacity : tail;
        capacity <<= 1;
        Object[] grow = new Object[capacity];

        System.arraycopy(elements, head, grow, 0, to - head);
        if (tail < head) {
            System.arraycopy(elements, 0, grow, to - head, tail);
        }

        elements = grow;
        head = 0;
        tail = size;
    }

    /**
     * Adds element to the queue
     *
     * Inv: tail == head, if queue is empty
     * Inv: tail > head, if size < (capacity - head)
     * Inv: tail < head, if size >= (capacity - head)
     *
     * Pre: el != null
     * Post: Adds element to the tail of queue (size' = size + 1), elements[tail] != null
     */
    public static void enqueue(Object el) {
        ensureCapacity(size + 1);
        elements[tail] = el;
        tail = (tail + 1) % capacity;
        size++;
    }

    /**
     * Returns first element at the queue
     *
     * Pre: Queue must contains at least one object (size > 0)
     * Post: Returns first element at the queue
     * Post: elements[head] != null
     */
    public static Object element() {
        assert size > 0;
        return elements[head];
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
    public static Object dequeue() {
        assert size > 0;
        size--;
        int from = head;
        head = (head + 1) % capacity;
        return elements[from];
    }

    /**
     * Returns current size of the queue
     *
     * Pre: -
     * Post: Returns current size of the queue
     */
    public static int size() {
        return size;
    }

    /**
     * Checks if the queue is empty
     *
     * Pre: -
     * Post: Returns a boolean, which indicated, that queue is empty, or not
     */
    public static boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all elements from the queue
     *
     * Pre: -
     * Post: Resets the queue to the initial state
     */
    public static void clear() {
        capacity = START_CAPACITY;
        size = 0;
        head = 0;
        tail = 0;
        elements = new Object[START_CAPACITY];
    }

    /**
     * Pre: -
     * Post: Returns a array with elements of queue, from first element to last
     */
    public static Object[] toArray() {
        int to = (tail < head) ? capacity : tail;
        Object[] arr = new Object[size];

        System.arraycopy(elements, head, arr, 0, to - head);
        if (tail < head) {
            System.arraycopy(elements, 0, arr, to - head, tail);
        }

        return arr;
    }
}