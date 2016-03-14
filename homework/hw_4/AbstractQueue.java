/**
 * Nariman Safiulin (woofilee)
 * File: AbstractQueue.java
 * Created on: Mar 14, 2016
 */

import java.util.function.Function;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {
    protected int size;

    protected abstract void pushRealization(Object element);

    protected abstract void enqueueRealization(Object element);

    protected abstract Object dequeueRealization();

    protected abstract Object removeRealization();

    @Override
    public void push(Object element) {
        this.pushRealization(element);
        this.size++;
    }

    @Override
    public void enqueue(Object element) {
        this.enqueueRealization(element);
        this.size++;
    }

    @Override
    public Object dequeue() {
        assert this.size > 0;
        Object needle = this.dequeueRealization();
        this.size--;
        return needle;
    }

    @Override
    public Object remove() {
        assert this.size > 0;
        Object needle = this.removeRealization();
        this.size--;
        return needle;
    }

    @Override
    public Object element() {
        Object needle = this.dequeue();
        this.push(needle);
        return needle;
    }

    @Override
    public Object peek() {
        Object needle = this.remove();
        this.enqueue(needle);
        return needle;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public void clear() {
        while (!isEmpty()) {
            this.dequeue();
        }
    }

    @Override
    public AbstractQueue filter(Predicate<Object> predicate) throws IllegalAccessException, InstantiationException {
        AbstractQueue result = this.getClass().newInstance();
        int count = this.size;

        while (count-- > 0) {
            Object el = this.dequeue();
            this.enqueue(el);

            if (predicate.test(el)) {
                result.enqueue(el);
            }
        }

        return result;
    }

    @Override
    public AbstractQueue map(Function<Object, Object> function) throws IllegalAccessException, InstantiationException {
        AbstractQueue result = this.getClass().newInstance();
        int count = this.size;

        while (count-- > 0) {
            Object el = this.dequeue();
            this.enqueue(el);
            result.enqueue(function.apply(el));
        }

        return result;
    }
}
