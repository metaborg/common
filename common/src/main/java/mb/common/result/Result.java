package mb.common.result;

import mb.common.option.Option;
import mb.common.util.UncheckedException;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;

import java.io.Serializable;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * A type that is either a value of type {@link T}, or an error of type {@link E}.
 *
 * @param <T> Type of values.
 * @param <E> Type of errors, descendants of {@link Exception}.
 * @apiNote Only {@link Serializable} when {@link T} and {@link E} are {@link Serializable}.
 */
@SuppressWarnings("unused") public interface Result<T, E extends Exception> extends Serializable {
    static <T, E extends Exception> Result<T, E> ofOk(T value) {
        return new Ok<>(value);
    }

    static <T, E extends Exception> Result<T, E> ofErr(E error) {
        return new Err<>(error);
    }


    static <T, E extends Exception> Result<T, E> ofNullableOrElse(@Nullable T value, Supplier<? extends E> def) {
        if(value != null) {
            return new Ok<>(value);
        } else {
            return new Err<>(def.get());
        }
    }

    static <T> Result<T, ExpectException> ofNullableOrExpect(@Nullable T value, String message) {
        if(value != null) {
            return new Ok<>(value);
        } else {
            return new Err<>(new ExpectException(message));
        }
    }

    static <T> Result<T, ExpectException> ofNullableOrExpect(@Nullable T value, String message, @Nullable Throwable cause) {
        if(value != null) {
            return new Ok<>(value);
        } else {
            return new Err<>(new ExpectException(message, cause));
        }
    }

    static <T> Result<T, ExpectException> ofNullableOrElseExpect(@Nullable T value, Supplier<String> messageSupplier) {
        if(value != null) {
            return new Ok<>(value);
        } else {
            return new Err<>(new ExpectException(messageSupplier.get()));
        }
    }

    static <T> Result<T, ExpectException> ofNullableOrElseExpect(@Nullable T value, Supplier<String> messageSupplier, Supplier<@Nullable Throwable> causeSupplier) {
        if(value != null) {
            return new Ok<>(value);
        } else {
            return new Err<>(new ExpectException(messageSupplier.get(), causeSupplier.get()));
        }
    }

    static <T, E extends Exception> Result<T, ? extends Exception> ofOkOrCatching(ThrowingSupplier<? extends T, E> supplier) {
        return ResultUtil.tryCatch(() -> Result.ofOk(supplier.get()), Result::ofErr);
    }

    static <T, E extends Exception> Result<T, E> ofOkOrCatching(ThrowingSupplier<? extends T, E> supplier, Class<E> exceptionClass) {
        return ResultUtil.tryCatch(() -> Result.ofOk(supplier.get()), Result::ofErr, exceptionClass);
    }


    @EnsuresNonNullIf(expression = "get()", result = true)
    @EnsuresNonNullIf(expression = "getErr()", result = false)
    @Pure boolean isOk();

    @Pure Option<T> ok();

    default Result<T, E> ifOk(Consumer<? super T> consumer) {
        ok().ifSome(consumer);
        return this;
    }

    default <F extends Exception> Result<T, E> ifOkThrowing(ThrowingConsumer<? super T, F> consumer) throws F {
        ok().ifSomeThrowing(consumer);
        return this;
    }


    @EnsuresNonNullIf(expression = "get()", result = false)
    @EnsuresNonNullIf(expression = "getErr()", result = true)
    @Pure boolean isErr();

    @Pure Option<E> err();

    default Result<T, E> ifErr(Consumer<? super E> consumer) {
        err().ifSome(consumer);
        return this;
    }

    default <F extends Exception> Result<T, E> ifErrThrowing(ThrowingConsumer<? super E, F> consumer) throws F {
        err().ifSomeThrowing(consumer);
        return this;
    }


    default Result<T, E> throwIfError() throws E {
        if(isErr()) {
            // noinspection ConstantConditions (`getErr` is safe because error is present if `isErr` returns true)
            throw getErr();
        }
        return this;
    }

    default Result<T, E> throwUncheckedIfError() {
        if(isErr()) {
            // noinspection ConstantConditions (`getErr` is safe because error is present if `isErr` returns true)
            throw new UncheckedException(getErr());
        }
        return this;
    }


    default void ifElse(Consumer<? super T> okConsumer, Consumer<? super E> errConsumer) {
        if(isOk()) {
            okConsumer.accept(get());
        } else {
            errConsumer.accept(getErr());
        }
    }

    default void ifElse(Consumer<? super T> okConsumer, Runnable errRunner) {
        if(isOk()) {
            okConsumer.accept(get());
        } else {
            errRunner.run();
        }
    }

    default <F extends Exception> void ifThrowingElse(ThrowingConsumer<? super T, F> okConsumer, Consumer<? super E> errConsumer) throws F {
        if(isOk()) {
            okConsumer.accept(get());
        } else {
            errConsumer.accept(getErr());
        }
    }

    default <F extends Exception> void ifThrowingElse(ThrowingConsumer<? super T, F> okConsumer, Runnable errRunner) throws F {
        if(isOk()) {
            okConsumer.accept(get());
        } else {
            errRunner.run();
        }
    }

    default <F extends Exception> void ifElseThrowing(Consumer<? super T> okConsumer, ThrowingConsumer<? super E, F> errConsumer) throws F {
        if(isOk()) {
            okConsumer.accept(get());
        } else {
            errConsumer.accept(getErr());
        }
    }

    default <F extends Exception> void ifElseThrowing(Consumer<? super T> okConsumer, ThrowingRunnable<F> errRunner) throws F {
        if(isOk()) {
            okConsumer.accept(get());
        } else {
            errRunner.run();
        }
    }

    default <F extends Exception, G extends Exception> void ifThrowingElseThrowing(ThrowingConsumer<? super T, F> okConsumer, ThrowingConsumer<? super E, G> errConsumer) throws F, G {
        if(isOk()) {
            okConsumer.accept(get());
        } else {
            errConsumer.accept(getErr());
        }
    }

    default <F extends Exception, G extends Exception> void ifThrowingElseThrowing(ThrowingConsumer<? super T, F> okConsumer, ThrowingRunnable<G> errRunner) throws F, G {
        if(isOk()) {
            okConsumer.accept(get());
        } else {
            errRunner.run();
        }
    }


    default <U> Result<U, E> map(Function<? super T, ? extends U> mapper) {
        if(isOk()) {
            return Result.ofOk(mapper.apply(get()));
        } else {
            // noinspection unchecked (cast is safe because it is impossible to get a value of type U in the err case)
            return (Result<U, E>)this;
        }
    }

    default <U, F extends Exception> Result<U, E> mapThrowing(ThrowingFunction<? super T, ? extends U, F> mapper) throws F {
        if(isOk()) {
            // noinspection ConstantConditions (`get` is safe because value is present if `isOk` returns true)
            return Result.ofOk(mapper.apply(get()));
        } else {
            // noinspection unchecked (cast is safe because it is impossible to get a value of type U in the err case)
            return (Result<U, E>)this;
        }
    }

    default <U> Result<U, ? extends Exception> mapCatching(ThrowingFunction<? super T, ? extends U, ? extends Exception> mapper) {
        if(isOk()) {
            // noinspection ConstantConditions (`get` is safe because value is present if `isOk` returns true)
            return ResultUtil.tryCatch(() -> Result.ofOk(mapper.apply(get())), Result::ofErr);
        } else {
            // noinspection unchecked (cast is safe because it is impossible to get a value of type U in the err case
            return (Result<U, ? extends Exception>)this;
        }
    }

    default <U, F extends Exception> Result<U, F> mapCatching(ThrowingFunction<? super T, ? extends U, F> mapper, Class<F> exceptionClass) {
        if(isOk()) {
            //noinspection ConstantConditions (`get` is safe because value is present if `isOk` returns true)
            return ResultUtil.tryCatch(() -> Result.ofOk(mapper.apply(get())), Result::ofErr, exceptionClass);
        } else {
            // noinspection unchecked (cast is safe because it is impossible to get a value of type U in the err case)
            return (Result<U, F>)this;
        }
    }

    default <U, F extends Exception> Result<U, F> mapCatchingOrRethrow(ThrowingFunction<? super T, ? extends U, Exception> mapper, Class<F> exceptionClass) throws Exception {
        if(isOk()) {
            //noinspection ConstantConditions (`get` is safe because value is present if `isOk` returns true)
            return ResultUtil.tryCatchOrRethrow(() -> Result.ofOk(mapper.apply(get())), Result::ofErr, exceptionClass);
        } else {
            // noinspection unchecked (cast is safe because it is impossible to get a value of type U in the err case)
            return (Result<U, F>)this;
        }
    }

    default <U> U mapOr(Function<? super T, ? extends U> mapper, U def) {
        return ok().mapOr(mapper, def);
    }

    default <U> @Nullable U mapOrNull(Function<? super T, ? extends U> mapper) {
        return ok().mapOrNull(mapper);
    }

    default <U> U mapOrThrow(Function<? super T, ? extends U> mapper) throws E {
        // `getErr` is safe because error is present if not ok case.
        return ok().mapOrElseThrow(mapper, this::getErr);
    }

    default <U> U mapOrElse(
        Function<? super T, ? extends U> mapper,
        Supplier<? extends U> def
    ) {
        return ok().mapOrElse(mapper, def);
    }

    default <U> U mapOrElse(
        Function<? super T, ? extends U> mapper,
        Function<? super E, ? extends U> def
    ) {
        // `getErr` is safe because error is present if not ok case.
        return ok().mapOrElse(mapper, () -> def.apply(getErr()));
    }

    default <U, F extends Exception> U mapThrowingOrElse(
        ThrowingFunction<? super T, ? extends U, F> mapper,
        Function<? super E, ? extends U> def
    ) throws F {
        // `getErr` is safe because error is present if not ok case.
        return ok().mapThrowingOrElse(mapper, () -> def.apply(getErr()));
    }

    default <U, F extends Exception> U mapOrElseThrow(
        Function<? super T, ? extends U> mapper,
        Function<? super E, ? extends F> def
    ) throws F {
        // `getErr` is safe because error is present if not ok case.
        return ok().mapOrElseThrow(mapper, () -> def.apply(getErr()));
    }


    default <F extends Exception> Result<T, F> mapErr(Function<? super E, ? extends F> mapper) {
        // noinspection unchecked (cast is safe because it is impossible to get a value of type F in the ok case)
        return err()
            .map(e -> Result.<T, F>ofErr(mapper.apply(e)))
            .unwrapOrElse(() -> (Result<T, F>)this);
    }

    default <F> F mapErrOr(Function<? super E, ? extends F> mapper, F def) {
        return err().mapOr(mapper, def);
    }

    default <F> @Nullable F mapErrOrNull(Function<? super E, ? extends F> mapper) {
        return err().mapOrNull(mapper);
    }

    default <F> F mapErrOrElse(
        Function<? super E, ? extends F> mapper,
        Supplier<? extends F> def
    ) {
        return err().mapOrElse(mapper, def);
    }

    default <F> F mapErrOrElse(
        Function<? super E, ? extends F> mapper,
        Function<? super T, ? extends F> def
    ) {
        // `get` is safe because value is present if not err case.
        return err().mapOrElse(mapper, () -> def.apply(get()));
    }


    default <U> Result<U, E> flatMap(Function<? super T, ? extends Result<U, E>> mapper) {
        // noinspection unchecked (cast is safe because it is impossible to get a value of type U in the err case)
        return ok().mapOrElse(mapper, () -> (Result<U, E>)this);
    }

    default <U, F extends Exception> Result<U, E> flatMapThrowing(
        ThrowingFunction<? super T, ? extends Result<U, E>, F> mapper
    ) throws F {
        // noinspection unchecked (cast is safe because it is impossible to get a value of type U in the err case)
        return ok().mapThrowingOrElse(mapper, () -> (Result<U, E>)this);
    }

    default <U, F extends Exception> Result<U, F> flatMapOrElse(
        Function<? super T, ? extends Result<U, F>> okMapper,
        Function<? super E, ? extends Result<U, F>> errMapper
    ) {
        // `getErr` is safe because error is present if not ok case.
        return ok().mapOrElse(okMapper, () -> errMapper.apply(getErr()));
    }

    default <U, F extends Exception, G extends Exception> Result<U, F> flatMapThrowingOrElse(
        ThrowingFunction<? super T, ? extends Result<U, F>, G> okMapper,
        Function<? super E, ? extends Result<U, F>> errMapper
    ) throws G {
        // `getErr` is safe because error is present if not ok case.
        return ok().mapThrowingOrElse(okMapper, () -> errMapper.apply(getErr()));
    }

    default <U, F extends Exception, G extends Exception> Result<U, F> flatMapOrElseThrowing(
        Function<? super T, ? extends Result<U, F>> okMapper,
        ThrowingFunction<? super E, ? extends Result<U, F>, G> errMapper
    ) throws G {
        // noinspection ConstantConditions (`getErr` is safe because error is present if not ok case)
        return ok().mapOrElseThrowing(okMapper, () -> errMapper.apply(getErr()));
    }

    default <U, F extends Exception, G extends Exception, H extends Exception> Result<U, F> flatMapThrowingOrElseThrowing(
        ThrowingFunction<? super T, ? extends Result<U, F>, G> okMapper,
        ThrowingFunction<? super E, ? extends Result<U, F>, H> errMapper
    ) throws G, H {
        // noinspection ConstantConditions (`getErr` is safe because error is present if not ok case)
        return ok().mapThrowingOrElseThrowing(okMapper, () -> errMapper.apply(getErr()));
    }


    default <U> Result<U, E> and(Result<U, E> other) {
        if(isErr()) {
            // noinspection unchecked (cast is safe because it is impossible to get a value of type U in the err case)
            return (Result<U, E>)this;
        }
        return other;
    }

    default <F extends Exception> Result<T, F> or(Result<T, F> other) {
        if(isOk()) {
            // noinspection unchecked (cast is safe because it is impossible to get a value of type F in the ok case)
            return (Result<T, F>)this;
        }
        return other;
    }


    default T unwrap() throws E {
        // noinspection ConstantConditions (`get` is safe because error is present if not ok case)
        return ok().unwrapOrElseThrow(() -> err().get());
    }

    default T unwrapUnchecked() {
        // noinspection ConstantConditions (`get` is safe because error is present if not ok case)
        return ok().unwrapOrElseThrow(() -> new UncheckedException(err().get()));
    }

    default T unwrapOr(T def) {
        return ok().unwrapOr(def);
    }

    default T unwrapOrElse(Supplier<? extends T> def) {
        return ok().unwrapOrElse(def);
    }

    default T expect(String message) {
        return ok().unwrapOrElseThrow(() -> new ExpectException(message, err().get()));
    }

    default T expect(Supplier<String> messageSupplier) {
        return ok().unwrapOrElseThrow(() -> new ExpectException(messageSupplier.get(), err().get()));
    }

    default <F extends Exception> T expect(Function<E, F> mapper) throws F {
        // noinspection ConstantConditions (`get` is safe because error is present if not ok case)
        return ok().unwrapOrElseThrow(() -> mapper.apply(err().get()));
    }


    default E unwrapErr() {
        return err().unwrapOrElseThrow(() -> new RuntimeException("Called `unwrapErr` on an `Ok` result"));
    }

    default E unwrapErrOr(E def) {
        return err().unwrapOr(def);
    }

    default E unwrapErrOrElse(Supplier<? extends E> def) {
        return err().unwrapOrElse(def);
    }

    default E expectErr(String message) {
        return err().unwrapOrElseThrow(() -> new ExpectException(message));
    }

    default E expectErr(Supplier<String> messageSupplier) {
        return err().unwrapOrElseThrow(() -> new ExpectException(messageSupplier.get()));
    }


    @Pure default @Nullable T get() {
        return ok().get();
    }

    default @Nullable T getOr(@Nullable T def) {
        return ok().getOr(def);
    }

    default @Nullable T getOrElse(Supplier<? extends @Nullable T> def) {
        return ok().getOrElse(def);
    }


    @Pure default @Nullable E getErr() {
        return err().get();
    }

    default @Nullable E getErrOr(@Nullable E def) {
        return err().getOr(def);
    }

    default @Nullable E getErrOrElse(Supplier<? extends @Nullable E> def) {
        return err().getOrElse(def);
    }


    static <T, E extends Exception> Result<Option<T>, E> transpose(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<Result<T, E>> option) {
        return transpose(Option.ofOptional(option));
    }

    static <T, E extends Exception> Result<Option<T>, E> transpose(Option<Result<T, E>> option) {
        return option.mapOrElse(r -> r.mapOrElse(v -> Result.ofOk(Option.ofSome(v)), Result::ofErr), () -> Result.ofOk(Option.ofNone()));
    }


    class Ok<T, E extends Exception> implements Result<T, E>, Serializable {
        public final T value;

        public Ok(T value) {
            this.value = value;
        }


        @Override public boolean isOk() {
            return true;
        }

        @Override public Option<T> ok() {
            return Option.ofSome(value);
        }

        @Override public boolean isErr() {
            return false;
        }

        @Override public Option<E> err() {
            return Option.ofNone();
        }


        @Override public boolean equals(@Nullable Object o) {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
            final Ok<?, ? extends Exception> ok = (Ok<?, ? extends Exception>)o;
            return value.equals(ok.value);
        }

        @Override public int hashCode() {
            return value.hashCode();
        }

        @Override public String toString() {
            return "Ok(" + value + ")";
        }
    }

    class Err<T, E extends Exception> implements Result<T, E>, Serializable {
        public final E error;

        public Err(E error) {
            this.error = error;
        }


        @Override public boolean isOk() {
            return false;
        }

        @Override public Option<T> ok() {
            return Option.ofNone();
        }

        @Override public boolean isErr() {
            return true;
        }

        @Override public Option<E> err() {
            return Option.ofSome(error);
        }


        @Override public boolean equals(@Nullable Object o) {
            if(this == o) return true;
            if(o == null || getClass() != o.getClass()) return false;
            final Err<?, ? extends Exception> err = (Err<?, ? extends Exception>)o;
            return error.equals(err.error);
        }

        @Override public int hashCode() {
            return error.hashCode();
        }

        @Override public String toString() {
            return "Err(" + error + ")";
        }
    }
}
