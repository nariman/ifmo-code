package queue;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public class QueueToArrayTest extends ArrayQueueToArrayTest {
    public static void main(final String[] args) {
        new QueueToArrayTest().test();
    }

    @Override
    protected ToArrayQueue create(final String className, final Mode mode) {
        return QueueTest.check(super.create(className, mode));
    }

    public void test() {
        test("LinkedQueue", 2, Mode.CLASS);
        test("ArrayQueue", 2, Mode.CLASS);
    }
}
