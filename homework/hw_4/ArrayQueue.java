/**
 * Nariman Safiulin (woofilee)
 * File: ArrayQueue.java
 * Created on: Mar 14, 2016
 */

public class ArrayQueue extends AbstractQueue {
    private static final int START_CAPACITY = 1 << 10;

    private int capacity, head, tail;
    private Object[] elements;

    public ArrayQueue() {
        this.capacity = START_CAPACITY;
        this.head = this.tail = 0;
        this.elements = new Object[START_CAPACITY];
    }

    /**
     * Checks, that current capacity of array is enough to append a new element.
     * Otherwise, doubles a capacity of array, and copies existing data in order from zero-cell of array.
     * <p>
     * Pre: -
     * Post: Doubles a capacity, if required capacity greater than current capacity, otherwise, returns
     * Inv: If doubles, head < tail
     *
     * @param newCapacity int     Required size of array
     */
    private void ensureCapacity(int newCapacity) {
        if (newCapacity < this.capacity) {
            return;
        }

        int last = (this.tail < this.head) ? this.capacity : this.tail;
        this.capacity <<= 1;
        Object[] grow = new Object[this.capacity];

        System.arraycopy(this.elements, this.head, grow, 0, last - this.head);
        if (this.tail < this.head) {
            System.arraycopy(this.elements, 0, grow, last - this.head, this.tail);
        }

        this.elements = grow;
        this.head = 0;
        this.tail = this.size;
    }

    @Override
    protected void pushRealization(Object element) {
        this.ensureCapacity(this.size + 1);
        this.head = (this.head - 1 >= 0) ? this.head - 1 : this.capacity - 1;
        this.elements[this.head] = element;
    }

    @Override
    protected void enqueueRealization(Object element) {
        this.ensureCapacity(this.size + 1);
        this.elements[this.tail] = element;
        this.tail = (this.tail + 1) % this.capacity;
    }

    @Override
    public Object element() {
        return this.elements[this.head];
    }

    @Override
    public Object peek() {
        return this.elements[(this.tail - 1 >= 0) ? this.tail - 1 : this.capacity - 1];
    }

    @Override
    protected void dequeueRealization() {
        this.elements[this.head] = null;
        this.head = (this.head + 1) % this.capacity;
    }

    @Override
    protected void removeRealization() {
        this.tail = (this.tail - 1 >= 0) ? this.tail - 1 : this.capacity - 1;
        this.elements[this.tail] = null;
    }

    @Override
    public void clear() {
        this.capacity = START_CAPACITY;
        this.size = this.head = this.tail = 0;
        this.elements = new Object[START_CAPACITY];
    }

//    @Override
//    public Object[] toArray() {
//        int last = (this.tail < this.head) ? this.capacity : this.tail;
//
//        System.arraycopy(this.elements, this.head, elements, 0, last - this.head);
//        if (this.tail < this.head) {
//            System.arraycopy(this.elements, 0, elements, last - this.head, this.tail);
//        }
//
//        return elements;
//    }
}
