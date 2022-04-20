package mb.common.function;

@FunctionalInterface
public interface Action1Throwing1<T1, E1 extends Exception>
    extends Action1Throwing2<T1, E1, E1> {
    void apply(T1 t1) throws E1;
}
