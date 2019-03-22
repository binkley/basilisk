package hm.binkley.basilisk.store;

import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.stream.Stream;

@RequiredArgsConstructor
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
public abstract class StandardStore<T extends StandardRecord<T, R, S>,
        R extends StandardRepository<T, R, S>,
        S extends StandardStore<T, R, S>> {
    protected final R springData;

    public final Optional<T> byId(final Long id) {
        return springData.findById(id)
                .map(this::assign);
    }

    public final Optional<T> byCode(final String code) {
        return springData.findByCode(code)
                .map(this::assign);
    }

    public final Stream<T> all() {
        return springData.readAll()
                .map(this::assign);
    }

    public T save(final T record) {
        return springData.save(record);
    }

    @SuppressWarnings("unchecked")
    protected final T assign(final T record) {
        record.store = (S) this;
        return record;
    }
}
