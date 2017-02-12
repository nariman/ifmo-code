package queue;

import java.util.Arrays;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class QueueTest<T extends ArrayQueueTest.Queue> extends ArrayQueueTest<T> {
    public QueueTest(final QueueFactory<T> factory) {
        super(factory);
    }

    public static void main(final String[] args) {
        new QueueTest<>(Queue::new).test();
    }

    public void test() {
        test("LinkedQueue", 2, Mode.CLASS);
        test("ArrayQueue", 2, Mode.CLASS);
    }

    @Override
    protected T create(final String className, final Mode mode) {
        return check(super.create(className, mode));
    }

    protected static <T extends Queue> T check(final T queue) {
        final Class<?> type = queue.instance.getClass();
        assertTrue(type + " should extend AbstractQueue", "AbstractQueue".equals(type.getSuperclass().getName()));
        assertTrue(type + " should implement interface Queue", implementsQueue(type) || implementsQueue(type.getSuperclass()));
        return queue;
    }

    private static boolean implementsQueue(final Class<?> clazz) {
        return Arrays.stream(clazz.getInterfaces()).anyMatch(iface -> "Queue".equals(iface.getName()));
    }
}
