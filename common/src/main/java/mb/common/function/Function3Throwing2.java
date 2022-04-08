package mb.common.function;

@FunctionalInterface
public interface Function3Throwing2<T1, T2, T3, R, E1 extends Exception, E2 extends Exception> {
    R apply(T1 t1, T2 t2, T3 t3) throws E1, E2;
}
