package mb.common.function;

@FunctionalInterface
public interface Function3<T1, T2, T3, R> extends
    Function3Throwing1<T1, T2, T3, R, RuntimeException>,
    Function3Throwing2<T1, T2, T3, R, RuntimeException, RuntimeException> {
    @Override R apply(T1 t1, T2 t2, T3 t3);
}
