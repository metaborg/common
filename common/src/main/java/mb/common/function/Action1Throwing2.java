package mb.common.function;

@FunctionalInterface
public interface Action1Throwing2<T1, E1 extends Exception, E2 extends Exception> {
    void apply(T1 t1) throws E1, E2;
}
