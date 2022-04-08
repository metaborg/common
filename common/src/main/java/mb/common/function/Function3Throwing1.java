package mb.common.function;

@FunctionalInterface
public interface Function3Throwing1<T1, T2, T3, R, E1 extends Exception>
    extends Function3Throwing2<T1, T2, T3, R, E1, E1> {
    @Override R apply(T1 t1, T2 t2, T3 t3) throws E1;
}
