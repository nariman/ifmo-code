/**
 * Nariman Safiulin (woofilee)
 * File: Queue.java
 * Created on: Mar 14, 2016
 */

import java.util.function.Function;
import java.util.function.Predicate;

public interface Queue {
    /**
     * Pre:  -
     * Post: Adds element to the head of the queue
     *       size' = size + 1 (Inv)
     *       queue'[2..size'] = queue[1..size]
     *       queue'[1] = element
     */
    void push(Object element);

    /**
     * Pre:  -
     * Post: Adds element to the tail of the queue
     *       size' = size + 1 (Inv)
     *       queue'[1..size] = queue[1..size]
     *       queue'[size'] = element
     */
    void enqueue(Object element);

    /**
     * Pre:  Queue must contains at least one element (size > 0)
     * Post: Returns the head element in the queue, without changes in queue
     *       R = queue[1]
     */
    Object element();

    /**
     * Pre:  Queue must contains at least one element (size > 0)
     * Post: Returns the tail element in the queue, without changes in queue
     *       R = queue[size]
     */
    Object peek();

    /**
     * Pre:  Queue must contains at least one element (size > 0)
     * Post: Returns the head element at the queue and removes it from the queue
     *       size' = size - 1 (Inv)
     *       queue' = queue[2..size]
     *       R = queue[1]
     */
    Object dequeue();

    /**
     * Pre:  Queue must contains at least one element (size > 0)
     * Post: Returns the tail element at the queue and removes it from the queue
     *       size' = size - 1 (Inv)
     *       queue' = queue[1..size']
     *       R = queue[size]
     */
    Object remove();

    /**
     * Pre:  -
     * Post: Returns current size of the queue, without changes in queue
     */
    int size();

    /**
     * Pre:  -
     * Post: Returns True, if queue is empty, otherwise False, without changes in queue
     */
    boolean isEmpty();

    /**
     * Pre:  -
     * Post: Removes all elements from the queue
     * Inv:  size' = 0
     */
    void clear();

    /**
     * Pre:  -
     * Post: Returns a array with elements of the queue, from the first element to the last, without changes in queue
     */
    Object[] toArray();

    /**
     * Pre:  -
     * Post: Returns a new queue with elements of the current queue, that satisfying the predicate, without changes in
     *       current queue
     *       R: queue'[], where for each el in queue'[]: predicate(array) returns true
     */
    AbstractQueue filter(Predicate<Object> predicate) throws IllegalAccessException, InstantiationException;

    /**
     * Pre:  -
     * Post: Returns a new queue with results of the function on the elements of the current queue, without changes in
     *       current queue
     *       R: queue'[], that for i = 1..size: queue'[i] = function(queue[i])
     */
    AbstractQueue map(Function<Object, Object> function) throws IllegalAccessException, InstantiationException;
}
