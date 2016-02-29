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

    private static void ensureCapacity(ArrayQueueADT queue, int newCapacity) {
        if (newCapacity < queue.capacity) {
            return;
        }

        int to = (queue.tail < queue.head) ? queue.capacity : queue.tail;
        queue.capacity <<= 1;
        Object[] grow = new Object[queue.capacity];

        for (int i = queue.head, j = 0; i < to; i++, j++) {
            grow[j] = queue.elements[i];
        }

        if (queue.tail < queue.head) {
            for (int i = 0, j = to - queue.head; i < queue.tail; i++, j++) {
                grow[j] = queue.elements[i];
            }
        }

        queue.elements = grow;
        queue.head = 0;
        queue.tail = queue.size;
    }

    /**
     * Adds element to the ArrayQueue
     */
    public static void enqueue(ArrayQueueADT queue, Object el) {
        assert el != null;
        ensureCapacity(queue, queue.size + 1);
        queue.elements[queue.tail] = el;
        queue.tail = (queue.tail + 1) % queue.capacity;
        queue.size++;
    }

    /**
     * Returns the first element in the ArrayQueue
     */
    public static Object element(ArrayQueueADT queue) {
        assert queue.size > 0;
        return queue.elements[queue.head];
    }

    /**
     * Remove and returns first element in the ArrayQueue
     */
    public static Object dequeue(ArrayQueueADT queue) {
        assert queue.size > 0;
        queue.size--;
        int from = queue.head;
        queue.head = (queue.head + 1) % queue.capacity;
        return queue.elements[from];
    }

    /**
     * Returns current size of ArrayQueue
     */
    public static int size(ArrayQueueADT queue) {
        return queue.size;
    }

    /**
     * Checks if the ArrayQueue is empty
     */
    public static boolean isEmpty(ArrayQueueADT queue) {
        return queue.size == 0;
    }

    /**
     * Removes all elements from the ArrayQueue
     */
    public static void clear(ArrayQueueADT queue) {
        queue.capacity = START_CAPACITY;
        queue.size = 0;
        queue.head = 0;
        queue.tail = 0;
        queue.elements = new Object[START_CAPACITY];
    }
}
