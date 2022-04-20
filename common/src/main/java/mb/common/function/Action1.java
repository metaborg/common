package mb.common.function;

@FunctionalInterface
public interface Action1<T1> extends
    Action1Throwing1<T1, RuntimeException>,
    Action1Throwing2<T1, RuntimeException, RuntimeException> {
    @Override void apply(T1 t1);
}
