package mb.common.function;

@FunctionalInterface
public interface Function1<T1, R> extends
    Function1Throwing1<T1, R, RuntimeException>,
    Function1Throwing2<T1, R, RuntimeException, RuntimeException> {
    @Override R apply(T1 t1);
}
