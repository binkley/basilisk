package hm.binkley.basilisk;

import hm.binkley.basilisk.store.StandardRecord;
import hm.binkley.basilisk.store.StandardRepository;
import hm.binkley.basilisk.store.StandardStore;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

@EqualsAndHashCode
@RequiredArgsConstructor
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
@ToString
public abstract class StandardFactory<T extends StandardRecord<T, R, S>,
        R extends StandardRepository<T, R, S>,
        S extends StandardStore<T, R, S>,
        D extends StandardDomain<T, R, S, D>> {
    // Keep these in *this* order, so ctor does not change
    // @formatter:off
    private final Function<T, D> ctor;
    protected final S store;
    // @formatter:on

    public final Optional<D> byId(final Long id) {
        return store.byId(id).map(ctor);
    }

    public final Optional<D> byCode(final String code) {
        return store.byCode(code).map(ctor);
    }

    public final Stream<D> all() {
        return store.all().map(ctor);
    }
}
