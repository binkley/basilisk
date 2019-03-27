package hm.binkley.basilisk.store;

import lombok.AllArgsConstructor;
import lombok.Generated;

import javax.validation.constraints.NotNull;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Optional;
import java.util.Spliterator;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.ToDoubleFunction;
import java.util.function.ToIntFunction;
import java.util.function.ToLongFunction;
import java.util.stream.Collector;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

@AllArgsConstructor
@Generated // Lie to JaCoCo
public class AutoClosingStream<T>
        implements Stream<T> {
    private @NotNull Stream<T> delegate;

    public static <T> Stream<T> autoClosing(final Stream<T> delegate) {
        return new AutoClosingStream<>(delegate);
    }

    @Override
    public @NotNull Stream<T> filter(final Predicate<? super T> predicate) {
        delegate = delegate.filter(predicate);
        return this;
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public @NotNull <R> Stream<R> map(
            final Function<? super T, ? extends R> mapper) {
        delegate = (Stream) delegate.map(mapper);
        return (Stream<R>) this;
    }

    @Override
    public @NotNull IntStream mapToInt(
            final ToIntFunction<? super T> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull LongStream mapToLong(
            final ToLongFunction<? super T> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull DoubleStream mapToDouble(
            final ToDoubleFunction<? super T> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public @NotNull <R> Stream<R> flatMap(
            final Function<? super T, ? extends Stream<? extends R>> mapper) {
        delegate = (Stream) delegate.flatMap(mapper);
        return (Stream<R>) this;
    }

    @Override
    public @NotNull IntStream flatMapToInt(
            final Function<? super T, ? extends IntStream> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull LongStream flatMapToLong(
            final Function<? super T, ? extends LongStream> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull DoubleStream flatMapToDouble(
            final Function<? super T, ? extends DoubleStream> mapper) {
        throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull Stream<T> distinct() {
        delegate = delegate.distinct();
        return this;
    }

    @Override
    public @NotNull Stream<T> sorted() {
        delegate = delegate.sorted();
        return this;
    }

    @Override
    public @NotNull Stream<T> sorted(final Comparator<? super T> comparator) {
        delegate = delegate.sorted(comparator);
        return this;
    }

    @Override
    public @NotNull Stream<T> peek(final Consumer<? super T> action) {
        delegate = delegate.peek(action);
        return this;
    }

    @Override
    public @NotNull Stream<T> limit(final long maxSize) {
        delegate = delegate.limit(maxSize);
        return this;
    }

    @Override
    public @NotNull Stream<T> skip(final long n) {
        delegate = delegate.skip(n);
        return this;
    }

    @Override
    public void forEach(final Consumer<? super T> action) {
        delegate.forEach(action);
        delegate.close();
    }

    @Override
    public void forEachOrdered(final Consumer<? super T> action) {
        delegate.forEachOrdered(action);
        delegate.close();
    }

    @Override
    public @NotNull Object[] toArray() {
        final var toArray = delegate.toArray();
        delegate.close();
        return toArray;
    }

    @Override
    public @NotNull <A> A[] toArray(final IntFunction<A[]> generator) {
        final var toArray = delegate.toArray(generator);
        delegate.close();
        return toArray;
    }

    @Override
    public @NotNull T reduce(final T identity,
            final BinaryOperator<T> accumulator) {
        final var reduce = delegate.reduce(identity, accumulator);
        delegate.close();
        return reduce;
    }

    @Override
    public @NotNull Optional<T> reduce(final BinaryOperator<T> accumulator) {
        final var reduce = delegate.reduce(accumulator);
        delegate.close();
        return reduce;
    }

    @Override
    public @NotNull <U> U reduce(final U identity,
            final BiFunction<U, ? super T, U> accumulator,
            final BinaryOperator<U> combiner) {
        final var reduce = delegate.reduce(identity, accumulator, combiner);
        delegate.close();
        return reduce;
    }

    @Override
    public @NotNull <R> R collect(final Supplier<R> supplier,
            final BiConsumer<R, ? super T> accumulator,
            final BiConsumer<R, R> combiner) {
        final var collect = delegate.collect(supplier, accumulator, combiner);
        delegate.close();
        return collect;
    }

    @Override
    public @NotNull <R, A> R collect(
            final Collector<? super T, A, R> collector) {
        final var collect = delegate.collect(collector);
        delegate.close();
        return collect;
    }

    @Override
    public @NotNull Optional<T> min(final Comparator<? super T> comparator) {
        final var min = delegate.min(comparator);
        delegate.close();
        return min;
    }

    @Override
    public @NotNull Optional<T> max(final Comparator<? super T> comparator) {
        final var max = delegate.max(comparator);
        delegate.close();
        return max;
    }

    @Override
    public long count() {
        final var count = delegate.count();
        delegate.close();
        return count;
    }

    @Override
    public boolean anyMatch(final Predicate<? super T> predicate) {
        final var anyMatch = delegate.anyMatch(predicate);
        delegate.close();
        return anyMatch;
    }

    @Override
    public boolean allMatch(final Predicate<? super T> predicate) {
        final var allMatch = delegate.allMatch(predicate);
        delegate.close();
        return allMatch;
    }

    @Override
    public boolean noneMatch(final Predicate<? super T> predicate) {
        final var noneMatch = delegate.noneMatch(predicate);
        delegate.close();
        return noneMatch;
    }

    @Override
    public @NotNull Optional<T> findFirst() {
        final var findFirst = delegate.findFirst();
        delegate.close();
        return findFirst;
    }

    @Override
    public @NotNull Optional<T> findAny() {
        final var findAny = delegate.findAny();
        delegate.close();
        return findAny;
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        final var iterator = delegate.iterator();
        delegate.close();
        return iterator;
    }

    @Override
    public @NotNull Spliterator<T> spliterator() {
        final var spliterator = delegate.spliterator();
        delegate.close();
        return spliterator;
    }

    @Override
    public boolean isParallel() {
        return delegate.isParallel();
    }

    @Override
    public @NotNull Stream<T> sequential() {
        delegate = delegate.sequential();
        return this;
    }

    @Override
    public @NotNull Stream<T> parallel() {
        delegate = delegate.parallel();
        return this;
    }

    @Override
    public @NotNull Stream<T> unordered() {
        delegate = delegate.unordered();
        return this;
    }

    @Override
    public @NotNull Stream<T> onClose(final Runnable closeHandler) {
        delegate = delegate.onClose(closeHandler);
        return this;
    }

    @Override
    public void close() {
        delegate.close();
    }
}
