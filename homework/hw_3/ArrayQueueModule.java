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
     * <p>
     * Pre: newCapacity > 0
     * Post: Doubles a capacity, if required capacity greater than current capacity, otherwise, returns
     * Inv: If doubles, head < tail
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
     * Pre: -
     * Post: Adds element to the head of the queue
     * Inv: size' = size + 1
     */
    public static void push(Object el) {
        ensureCapacity(size + 1);
        size++;

        if (head - 1 >= 0) {
            elements[--head] = el;
        } else {
            elements[capacity - 1] = el;
            head = capacity - 1;
        }
    }

    /**
     * Pre: -
     * Post: Adds element to the tail of the queue
     * Inv: size' = size + 1
     */
    public static void enqueue(Object el) {
        assert el != null;
        ensureCapacity(size + 1);
        size++;

        elements[tail] = el;
        tail = (tail + 1) % capacity;
    }

    /**
     * Pre: Queue must contains at least one element (size > 0)
     * Post: Returns the head element at the queue and removes it from the queue
     * Inv: size' = size - 1
     */
    public static Object dequeue() {
        assert size > 0;
        size--;

        int from = head;
        head = (head + 1) % capacity;
        return elements[from];
    }

    /**
     * Pre: Queue must contains at least one element (size > 0)
     * Post: Returns the tail element at the queue and removes it from the queue
     * Inv: size' = size - 1
     */
    public static Object remove() {
        assert size > 0;
        size--;

        tail = (tail - 1 >= 0) ? tail - 1 : capacity - 1;
        return elements[tail];
    }

    /**
     * Pre: Queue must contains at least one element (size > 0)
     * Post: Returns the head element in the queue, without changes in queue
     */
    public static Object element() {
        assert size > 0;
        return elements[head];
    }

    /**
     * Pre: Queue must contains at least one element (size > 0)
     * Post: Returns the tail element in the queue, without changes in queue
     */
    public static Object peek() {
        assert size > 0;
        return elements[(tail - 1 >= 0) ? tail - 1 : capacity - 1];
    }

    /**
     * Pre: -
     * Post: Returns current size of the queue, without changes in queue
     */
    public static int size() {
        return size;
    }

    /**
     * Pre: -
     * Post: Returns True, if queue is empty, otherwise False, without changes in queue
     */
    public static boolean isEmpty() {
        return size == 0;
    }

    /**
     * Pre: -
     * Post: Removes all elements from the queue
     * Inv: size' = 0
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
     * Post: Returns a array with elements of the queue, from the first element to the last, without changes in queue
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