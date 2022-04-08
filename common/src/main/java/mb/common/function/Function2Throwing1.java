package mb.common.function;

@FunctionalInterface
public interface Function2Throwing1<T1, T2, R, E1 extends Exception>
    extends Function2Throwing2<T1, T2, R, E1, E1> {
    @Override R apply(T1 t1, T2 t2) throws E1;
}
