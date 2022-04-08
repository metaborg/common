package mb.common.result;

import mb.common.function.Function1Throwing1;

@FunctionalInterface
public interface ThrowingFunction<T, R, E extends Exception> extends Function1Throwing1<T, R, E> {

}
