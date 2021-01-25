package mb.common.result;

@FunctionalInterface
public interface ExceptionalConsumer<T, E extends Exception> {
    void accept(T t) throws E;
}
