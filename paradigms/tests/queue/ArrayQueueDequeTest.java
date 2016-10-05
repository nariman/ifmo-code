package queue;

import java.net.MalformedURLException;
import java.util.Deque;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class ArrayQueueDequeTest extends ArrayQueueTest<ArrayQueueDequeTest.QueueDeque> {
    public ArrayQueueDequeTest() {
        super(QueueDeque::new);
    }

    public static void main(final String[] args) {
        new ArrayQueueDequeTest().test();
    }

    @Override
    protected void add(final Deque<Object> deque, final QueueDeque queue, final Object element) {
        if (random.nextBoolean()) {
            super.add(deque, queue, element);
        } else {
            deque.addFirst(element);
            queue.push(element);
        }
    }

    @Override
    protected void check(final Deque<Object> deque, final QueueDeque queue) {
        if (random.nextBoolean()) {
            super.check(deque, queue);
        } else {
            assertEquals("peek()", deque.getLast(), queue.peek());
        }
    }

    @Override
    protected void remove(final Deque<Object> deque, final QueueDeque queue) {
        if (random.nextBoolean()) {
            super.remove(deque, queue);
        } else {
            assertEquals("remove()", deque.removeLast(), queue.remove());
        }
    }

    static class QueueDeque extends ArrayQueueTest.Queue {
        private final ZMethod<Void> push;
        private final ZMethod<Object> peek;
        private final ZMethod<Object> remove;

        public QueueDeque(final String className, final Mode mode) throws MalformedURLException, NoSuchMethodException, ClassNotFoundException {
            super(className, mode);
            push = findMethod("push", Object.class);
            peek = findMethod("peek");
            remove = findMethod("remove");
        }

        public void push(final Object element) {
            push.invoke(element);
        }

        public Object peek() {
            return peek.invoke();
        }

        public Object remove() {
            return remove.invoke();
        }
    }
}
