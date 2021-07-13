package mb.common.iterable;

import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

public class CompoundIterator<T> implements Iterator<T> {
    private final Iterator<? extends Iterable<? extends T>> iterables;
    private transient Iterator<? extends T> iterator = Collections.emptyIterator();


    public CompoundIterator(Iterator<? extends Iterable<? extends T>> iterables) {
        this.iterables = iterables;
    }


    @Override public boolean hasNext() {
        if(iterator.hasNext()) {
            return true;
        }
        try {
            iterator = iterables.next().iterator();
            return hasNext();
        } catch(NoSuchElementException ex) {
            return false;
        }
    }

    @Override public T next() {
        try {
            return iterator.next();
        } catch(NoSuchElementException ex) {
            iterator = iterables.next().iterator();
            return next();
        }
    }

    @Override public void remove() {
        throw new UnsupportedOperationException();
    }


    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final CompoundIterator<?> that = (CompoundIterator<?>)o;
        return iterables.equals(that.iterables);
    }

    @Override public int hashCode() {
        return iterables.hashCode();
    }

    @Override public String toString() {
        return "CompoundIterator{" +
            "iterables=" + iterables +
            ", iterator=" + iterator +
            '}';
    }
}
