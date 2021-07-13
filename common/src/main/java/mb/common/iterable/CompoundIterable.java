package mb.common.iterable;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Iterator;

public class CompoundIterable<T> implements Iterable<T> {
    private final Iterable<? extends Iterable<? extends T>> iterables;


    public CompoundIterable(Iterable<? extends Iterable<? extends T>> iterables) {
        this.iterables = iterables;
    }


    @Override public Iterator<T> iterator() {
        return new CompoundIterator<>(iterables.iterator());
    }


    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final CompoundIterable<?> that = (CompoundIterable<?>)o;
        return iterables.equals(that.iterables);
    }

    @Override public int hashCode() {
        return iterables.hashCode();
    }

    @Override public String toString() {
        return "CompoundIterable{" +
            "iterables=" + iterables +
            '}';
    }
}
