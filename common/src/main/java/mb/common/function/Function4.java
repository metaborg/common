package mb.common.function;

@FunctionalInterface
public interface Function4<T1, T2, T3, T4, R> extends
    Function4Throwing1<T1, T2, T3, T4, R, RuntimeException>,
    Function4Throwing2<T1, T2, T3, T4, R, RuntimeException, RuntimeException> {
    @Override R apply(T1 t1, T2 t2, T3 t3, T4 t4);
}
