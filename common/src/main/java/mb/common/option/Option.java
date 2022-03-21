package mb.common.option;

import mb.common.result.Result;
import mb.common.result.ResultUtil;
import mb.common.result.ThrowingConsumer;
import mb.common.result.ThrowingFunction;
import mb.common.result.ThrowingPredicate;
import mb.common.result.ThrowingRunnable;
import mb.common.result.ThrowingSupplier;
import org.checkerframework.checker.nullness.qual.EnsuresNonNullIf;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.checkerframework.dataflow.qual.Pure;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * An option type that supports serialization with more functional mappers.
 *
 * @param <T> Types of values.
 * @apiNote Only {@link Serializable} when {@link T} is {@link Serializable}.
 */
public class Option<T> implements Serializable {
    private static final Option<?> NONE = new Option<>();

    private final @Nullable T value;


    private Option() {
        this.value = null;
    }

    private Option(T value) {
        this.value = Objects.requireNonNull(value);
    }


    public static <T> Option<T> ofNone() {
        @SuppressWarnings("unchecked") final Option<T> empty = (Option<T>)NONE;
        return empty;
    }

    public static <T> Option<T> ofSome(T value) {
        return new Option<>(value);
    }

    public static <T> Option<T> ofNullable(@Nullable T value) {
        return value == null ? ofNone() : ofSome(value);
    }

    public static <T> Option<T> ofOptional(@SuppressWarnings("OptionalUsedAsFieldOrParameterType") Optional<T> value) {
        return value.map(Option::ofSome).orElseGet(Option::ofNone);
    }


    @EnsuresNonNullIf(expression = "get()", result = true)
    @Pure public boolean isSome() {
        return value != null;
    }

    public Option<T> ifSome(Consumer<? super T> consumer) {
        if(value != null) {
            consumer.accept(value);
        }
        return this;
    }

    public <F extends Exception> Option<T> ifSomeThrowing(ThrowingConsumer<? super T, F> consumer) throws F {
        if(value != null) {
            consumer.accept(value);
        }
        return this;
    }

    public boolean isNone() {
        return value == null;
    }

    public Option<T> ifNone(Runnable runnable) {
        if(value == null) {
            runnable.run();
        }
        return this;
    }

    public <F extends Exception> Option<T> ifNoneThrowing(ThrowingRunnable<F> runnable) throws F {
        if(value == null) {
            runnable.run();
        }
        return this;
    }

    public Option<T> ifElse(Consumer<? super T> someConsumer, Runnable noneRunnable) {
        if(isSome()) {
            someConsumer.accept(value);
        } else {
            noneRunnable.run();
        }
        return this;
    }


    public <U> Option<U> map(Function<? super T, ? extends U> mapper) {
        return value != null ? Option.ofSome(mapper.apply(value)) : ofNone();
    }

    public <U, F extends Exception> Option<U> mapThrowing(ThrowingFunction<? super T, ? extends U, F> mapper) throws F {
        return value != null ? Option.ofSome(mapper.apply(value)) : ofNone();
    }

    public <U> Option<Result<U, ? extends Exception>> mapCatching(ThrowingFunction<? super T, ? extends U, ? extends Exception> mapper) {
        return value != null ? ofSome(ResultUtil.tryCatch(() -> Result.ofOk(mapper.apply(value)), Result::ofErr)) : ofNone();
    }

    public <U, F extends Exception> Option<Result<U, F>> mapCatching(ThrowingFunction<? super T, ? extends U, F> mapper, Class<F> exceptionClass) {
        return value != null ? ofSome(ResultUtil.tryCatch(() -> Result.ofOk(mapper.apply(value)), Result::ofErr, exceptionClass)) : ofNone();
    }

    public <U> U mapOr(Function<? super T, ? extends U> mapper, U def) {
        return value != null ? mapper.apply(value) : def;
    }

    public <U, F extends Exception> U mapThrowingOr(ThrowingFunction<? super T, ? extends U, F> mapper, U def) throws F {
        return value != null ? mapper.apply(value) : def;
    }

    public <U> @Nullable U mapOrNull(Function<? super T, ? extends U> mapper) {
        return value != null ? mapper.apply(value) : null;
    }

    public <U, F extends Exception> @Nullable U mapThrowingOrNull(ThrowingFunction<? super T, ? extends U, F> mapper) throws F {
        return value != null ? mapper.apply(value) : null;
    }

    public <U> U mapOrElse(Function<? super T, ? extends U> mapper, Supplier<? extends U> def) {
        return value != null ? mapper.apply(value) : def.get();
    }

    public <U, F extends Exception> U mapThrowingOrElse(ThrowingFunction<? super T, ? extends U, F> mapper, Supplier<? extends U> def) throws F {
        return value != null ? mapper.apply(value) : def.get();
    }

    public <U, F extends Exception> U mapOrElseThrowing(Function<? super T, ? extends U> mapper, ThrowingSupplier<? extends U, F> def) throws F {
        return value != null ? mapper.apply(value) : def.get();
    }

    public <U, F extends Exception, G extends Exception> U mapThrowingOrElseThrowing(ThrowingFunction<? super T, ? extends U, F> mapper, ThrowingSupplier<? extends U, G> def) throws F, G {
        return value != null ? mapper.apply(value) : def.get();
    }

    public <U, E extends Throwable> U mapOrElseThrow(
        Function<? super T, ? extends U> mapper,
        Supplier<? extends E> exceptionSupplier
    ) throws E {
        if(value != null) {
            return mapper.apply(value);
        }
        throw exceptionSupplier.get();
    }

    public <U> Option<U> flatMap(Function<? super T, Option<U>> mapper) {
        return value != null ? mapper.apply(value) : ofNone();
    }

    public <U, F extends Exception> Option<U> flatMapThrowing(ThrowingFunction<? super T, Option<U>, F> mapper) throws F {
        return value != null ? mapper.apply(value) : ofNone();
    }


    public Option<T> filter(Predicate<? super T> predicate) {
        return value != null ? (predicate.test(value) ? this : ofNone()) : ofNone();
    }

    public <F extends Exception> Option<T> filterThrowing(ThrowingPredicate<? super T, F> predicate) throws F {
        return value != null ? (predicate.test(value) ? this : ofNone()) : ofNone();
    }


    public <U> Option<U> and(Option<U> other) {
        if(isNone()) {
            // noinspection unchecked (cast is safe because it is impossible to get a value of type U in the none case)
            return (Option<U>)this;
        }
        return other;
    }

    public Option<T> or(Option<T> other) {
        if(isSome()) {
            return this;
        }
        return other;
    }

    public Option<T> orElse(Supplier<Option<T>> other) {
        if(isSome()) {
            return this;
        }
        return other.get();
    }

    public Result<Option<T>, ? extends Exception> orElseCatching(ThrowingSupplier<Option<T>, ? extends Exception> other) {
        if(isSome()) {
            return Result.ofOk(this);
        }
        return ResultUtil.tryCatch(() -> Result.ofOk(other.get()), Result::ofErr);
    }

    public <F extends Exception> Result<Option<T>, F> orElseCatching(ThrowingSupplier<Option<T>, F> other, Class<F> exceptionClass) {
        if(isSome()) {
            return Result.ofOk(this);
        }
        return ResultUtil.tryCatch(() -> Result.ofOk(other.get()), Result::ofErr, exceptionClass);
    }


    public Stream<T> stream() {
        return value != null ? Stream.of(value) : Stream.empty();
    }


    public T unwrap() {
        if(value != null) {
            return value;
        }
        throw new NoSuchElementException("Called `unwrap` on a `None` value");
    }

    public T unwrapOr(T other) {
        return value != null ? value : other;
    }

    public T unwrapOrElse(Supplier<? extends T> other) {
        return value != null ? value : other.get();
    }

    public Result<T, ? extends Exception> unwrapOrElseCatching(ThrowingSupplier<? extends T, ? extends Exception> other) {
        return value != null ? Result.ofOk(value) : ResultUtil.tryCatch(() -> Result.ofOk(other.get()), Result::ofErr);
    }

    public <F extends Exception> Result<T, F> unwrapOrElseCatching(ThrowingSupplier<? extends T, F> other, Class<F> exceptionClass) {
        return value != null ? Result.ofOk(value) : ResultUtil.tryCatch(() -> Result.ofOk(other.get()), Result::ofErr, exceptionClass);
    }

    public <E extends Throwable> T unwrapOrElseThrow(Supplier<? extends E> exceptionSupplier) throws E {
        if(value != null) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }


    @Pure public @Nullable T get() {
        return value;
    }

    public @Nullable T getOr(@Nullable T def) {
        return value != null ? value : def;
    }

    public @Nullable T getOrElse(Supplier<? extends @Nullable T> def) {
        return value != null ? value : def.get();
    }


    public static <T, E extends Exception> Option<Result<T, E>> transpose(Result<Option<T>, E> result) {
        return result.mapOrElse(o -> o.mapOrElse(v -> Option.ofSome(Result.ofOk(v)), Option::ofNone), e -> Option.ofSome(Result.ofErr(e)));
    }

    public static <T> Option<ArrayList<T>> transpose(Collection<Option<T>> collection) {
        if(collection.stream().anyMatch(Option::isNone)) {
            return Option.ofNone();
        } else {
            return Option.ofSome(collection.stream().map(Option::unwrap).collect(Collectors.toCollection(ArrayList::new)));
        }
    }


    public Optional<T> toOptional() {
        return Optional.ofNullable(value);
    }


    @Override public boolean equals(@Nullable Object o) {
        if(this == o) return true;
        if(o == null || getClass() != o.getClass()) return false;
        final Option<?> option = (Option<?>)o;
        return Objects.equals(value, option.value);
    }

    @Override public int hashCode() {
        return Objects.hash(value);
    }

    @Override public String toString() {
        return value != null ? "Some(" + value + ")" : "None";
    }

    // TODO: deserialize none to shared none value
}
