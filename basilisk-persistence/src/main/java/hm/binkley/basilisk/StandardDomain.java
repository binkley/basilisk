package hm.binkley.basilisk;

import hm.binkley.basilisk.store.StandardRecord;
import hm.binkley.basilisk.store.StandardRepository;
import hm.binkley.basilisk.store.StandardStore;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@EqualsAndHashCode
@RequiredArgsConstructor
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
@ToString
public abstract class StandardDomain<T extends StandardRecord<T, R, S>,
        R extends StandardRepository<T, R, S>,
        S extends StandardStore<T, R, S>,
        D extends StandardDomain<T, R, S, D>>
        implements Codeable<D> {
    protected final T record;

    public final Long getId() { return record.getId(); }

    @Override
    public final @NotNull String getCode() { return record.getCode(); }

    @SuppressWarnings("unchecked")
    public final D save() {
        record.save();
        return (D) this;
    }

    @SuppressWarnings("unchecked")
    public final D delete() {
        record.delete();
        return (D) this;
    }
}
