package mb.common.function;

@FunctionalInterface
public interface Function1Throwing1<T1, R, E1 extends Exception>
    extends Function1Throwing2<T1, R, E1, E1> {
    R apply(T1 t1) throws E1;
}
