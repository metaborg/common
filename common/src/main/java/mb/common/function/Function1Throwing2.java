package mb.common.function;

@FunctionalInterface
public interface Function1Throwing2<T1, R, E1 extends Exception, E2 extends Exception> {
    R apply(T1 t1) throws E1, E2;
}
