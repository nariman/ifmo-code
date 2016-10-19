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

    protected abstract void dequeueRealization();

    protected abstract void removeRealization();

    protected abstract AbstractQueue getNewInstance();

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
        Object needle = this.element();
        this.dequeueRealization();
        this.size--;
        return needle;
    }

    @Override
    public Object remove() {
        assert this.size > 0;
        Object needle = this.peek();
        this.removeRealization();
        this.size--;
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
    public Object[] toArray() {
        Object[] elements = new Object[this.size];

        for (int i = 0; i < size; i++) {
            elements[i] = dequeue();
            enqueue(elements[i]);
        }

        return elements;
    }

    @Override
    public AbstractQueue filter(Predicate<Object> predicate) {
        AbstractQueue elements = getNewInstance();
        int count = this.size;

        while (count-- > 0) {
            Object el = this.dequeue();
            this.enqueue(el);

            if (predicate.test(el)) {
                elements.enqueue(el);
            }
        }

        return elements;
    }

    @Override
    public AbstractQueue map(Function<Object, Object> function) throws IllegalAccessException, InstantiationException {
        AbstractQueue elements = this.getClass().newInstance();
        int count = this.size;

        while (count-- > 0) {
            Object el = this.dequeue();
            this.enqueue(el);
            elements.enqueue(function.apply(el));
        }

        return elements;
    }
}
