package queue;

import java.net.MalformedURLException;
import java.util.Arrays;
import java.util.Deque;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class QueueFunctionsTest extends QueueTest<QueueFunctionsTest.QueueFunctions> {
    public QueueFunctionsTest() {
        super(QueueFunctions::new);
    }

    public static void main(final String[] args) {
        new QueueFunctionsTest().test();
    }

    @Override
    protected QueueFunctions create(final String className, final Mode mode) {
        return QueueTest.check(super.create(className, mode));
    }

    @Override
    protected void linearTest(final Deque<Object> deque, final QueueFunctions queue) {
        if (random.nextBoolean()) {
            final Predicate<Object> predicate = Predicate.isEqual(randomElement());
            assertEquals("filter()", deque.stream().filter(predicate), queue.filter(predicate));
        } else {
            final Function<Object, Object> f = random.nextBoolean() ? String::valueOf : Object::hashCode;
            assertEquals("map()", deque.stream().map(f), queue.map(f));
        }
    }

    private void assertEquals(final String message, final Stream<Object> expected, final Queue actual) {
        assertEquals(message, Arrays.asList(expected.toArray()), Arrays.asList(toArray(actual)));
    }

    private Object[] toArray(final Queue queue) {
        final Object[] result = new Object[queue.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = queue.dequeue();
        }
        return result;
    }

    static class QueueFunctions extends ArrayQueueTest.Queue {
        private final ZMethod<Object> filter;
        private final ZMethod<Object> map;

        public QueueFunctions(final String className, final Mode mode) throws MalformedURLException, NoSuchMethodException, ClassNotFoundException {
            super(className, mode);
            filter = findMethod("filter", Predicate.class);
            map = findMethod("map", Function.class);
        }

        public Queue filter(final Predicate p) {
            return wrap(filter.invoke(p));
        }

        public Queue map(final Function f) {
            return wrap(map.invoke(f));
        }

        private Queue wrap(final Object instance) {
            assertEquals("queue type", this.instance.getClass().getName(), instance.getClass().getName());
            try {
                return new Queue(instance, Mode.CLASS);
            } catch (final NoSuchMethodException e) {
                throw new AssertionError(e);
            }
        }
    }
}
