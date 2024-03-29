package mb.common.result;

import java.util.function.Function;

public class ResultUtil {
    public static <T> T tryCatch(ThrowingSupplier<T, ?> function, Function<? super Exception, T> exceptionFunction) {
        try {
            return function.get();
        } catch(RuntimeException e) {
            throw e;
        } catch(Exception e) {
            return exceptionFunction.apply(e);
        }
    }

    public static <T, E extends Exception> T tryCatch(ThrowingSupplier<T, E> supplier, Function<E, T> exceptionFunction, Class<E> exceptionClass) {
        try {
            return supplier.get();
        } catch(RuntimeException e) {
            throw e;
        } catch(Exception e) {
            if(e.getClass().equals(exceptionClass)) {
                // noinspection unchecked (cast is safe because `e`'s class is equal to `exceptionClass`)
                return exceptionFunction.apply((E)e);
            } else {
                // `e` is not of type `F`, and it cannot be another checked exception. Therefore, it is either a
                // `RuntimeException` or a checked exception that is sneakily thrown. In either case, it is safe to
                // sneakily rethrow the exception
                sneakyThrow(e);
                // Because `SneakyThrow.doThrow` throws, the following statement will never be executed, but it is
                // still needed to make the Java compiler happy.
                throw new RuntimeException(e);
            }
        }
    }

    static <T, E extends Exception> T tryCatchOrRethrow(ThrowingSupplier<T, Exception> supplier, Function<E, T> exceptionFunction, Class<E> exceptionClass) throws Exception {
        try {
            return supplier.get();
        } catch(RuntimeException e) {
            throw e;
        } catch(Exception e) {
            if(e.getClass().equals(exceptionClass)) {
                // If thrown exception is of `exceptionClass`, apply `exceptionFunction`
                // noinspection unchecked (cast is safe because `e`'s class is equal to `exceptionClass`)
                return exceptionFunction.apply((E)e);
            } else {
                // Otherwise rethrown the exception.
                throw e;
            }
        }
    }

    @SuppressWarnings("unchecked") static <T extends Throwable> void sneakyThrow(Throwable t) throws T {
        throw (T)t;
    }
}
