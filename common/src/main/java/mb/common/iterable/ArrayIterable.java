package mb.common.iterable;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Arrays;
import java.util.Iterator;

public class ArrayIterable<T> implements Iterable<T> {
    private final T[] array;


    @SafeVarargs public ArrayIterable(T... array) {
        this.array = array;
    }


    @Override public Iterator<T> iterator() {
        return new ArrayIterator<>(array);
    }


    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final ArrayIterable<?> that = (ArrayIterable<?>)o;
        return Arrays.equals(array, that.array);
    }

    @Override public int hashCode() {
        return Arrays.hashCode(array);
    }

    @Override public String toString() {
        return Arrays.toString(array);
    }
}
