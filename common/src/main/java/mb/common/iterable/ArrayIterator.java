package mb.common.iterable;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayIterator<T> implements Iterator<T> {
    private final T[] array;
    private transient int pos = 0;


    @SafeVarargs public ArrayIterator(T... array) {
        this.array = array;
    }


    @Override public boolean hasNext() {
        return pos < array.length;
    }

    @Override public T next() throws NoSuchElementException {
        if(hasNext())
            return array[pos++];
        else
            throw new NoSuchElementException();
    }

    @Override public void remove() {
        throw new UnsupportedOperationException();
    }


    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final ArrayIterator<?> that = (ArrayIterator<?>)o;
        return Arrays.equals(array, that.array);
    }

    @Override public int hashCode() {
        return Arrays.hashCode(array);
    }

    @Override public String toString() {
        return Arrays.toString(array) + "@" + pos;
    }
}
