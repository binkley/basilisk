package hm.binkley.basilisk.store;

import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@RequiredArgsConstructor
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class StandardStore<T extends StandardRecord<T, R, S>,
        R extends StandardRepository<T, R, S>,
        S extends StandardStore<T, R, S>> {
    protected final R springData;

    public final Optional<T> byId(final @NotNull Long id) {
        requireNonNull(id);
        return springData.findById(id)
                .map(this::bind);
    }

    public final Optional<T> byCode(final @NotNull String code) {
        requireNonNull(code);
        return springData.findByCode(code)
                .map(this::bind);
    }

    public final Stream<T> all() {
        return springData.readAll()
                .map(this::bind);
    }

    public T save(final @NotNull T record) {
        requireNonNull(record);
        return springData.save(record);
    }

    public T delete(final @NotNull T record) {
        requireNonNull(record);
        springData.delete(record);
        record.id = null;
        return record;
    }

    @SuppressWarnings("unchecked")
    protected final T bind(final T record) {
        record.store = (S) this;
        return record;
    }
}
