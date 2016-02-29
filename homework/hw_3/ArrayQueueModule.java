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

    private static void ensureCapacity(int newCapacity) {
        if (newCapacity < capacity) {
            return;
        }

        int to = (tail < head) ? capacity : tail;
        capacity <<= 1;
        Object[] grow = new Object[capacity];

        for (int i = head, j = 0; i < to; i++, j++) {
            grow[j] = elements[i];
        }

        if (tail < head) {
            for (int i = 0, j = to - head; i < tail; i++, j++) {
                grow[j] = elements[i];
            }
        }

        elements = grow;
        head = 0;
        tail = size;
    }

    /**
     * Adds element to the ArrayQueue
     */
    public static void enqueue(Object el) {
        assert el != null;
        ensureCapacity(size + 1);
        elements[tail] = el;
        tail = (tail + 1) % capacity;
        size++;
    }

    /**
     * Returns the first element in the ArrayQueue
     */
    public static Object element() {
        assert size > 0;
        return elements[head];
    }

    /**
     * Remove and returns first element in the ArrayQueue
     */
    public static Object dequeue() {
        assert size > 0;
        size--;
        int from = head;
        head = (head + 1) % capacity;
        return elements[from];
    }

    /**
     * Returns current size of ArrayQueue
     */
    public static int size() {
        return size;
    }

    /**
     * Checks if the ArrayQueue is empty
     */
    public static boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all elements from the ArrayQueue
     */
    public static void clear() {
        capacity = START_CAPACITY;
        size = 0;
        head = 0;
        tail = 0;
        elements = new Object[START_CAPACITY];
    }
}