package hm.binkley.basilisk.store;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.time.Instant;
import java.util.function.Supplier;

@EqualsAndHashCode(exclude = {"copy", "store"})
@ToString(exclude = {"copy", "store"})
public abstract class StandardRecord<T extends StandardRecord<T, R, S>,
        R extends StandardRepository<T, R, S>,
        S extends StandardStore<T, R, S>>
        implements Cloneable {
    @Transient
    private final Supplier<T> copy;

    @Getter
    @Id
    public Long id;
    @CreatedDate
    @Getter
    public Instant receivedAt;
    @Getter
    public String code;
    @Transient
    public S store;

    public StandardRecord(final Supplier<T> copy,
            final Long id, final Instant receivedAt, final String code) {
        this.copy = copy;
        this.id = id;
        this.receivedAt = receivedAt;
        this.code = code;
    }

    void become(final T other) {
        id = other.getId();
        receivedAt = other.getReceivedAt();
    }

    @SuppressWarnings("unchecked")
    public final T save() {
        return store.save((T) this);
    }

    @SuppressWarnings("unchecked")
    public final T delete() {
        return store.delete((T) this);
    }

    @Override
    @SuppressWarnings({
            "MethodDoesntCallSuperMethod",
            "PMD.CloneMethodReturnTypeMustMatchClassName",
            "PMD.CloneThrowsCloneNotSupportedException"})
    public final T clone() {
        return copy.get();
    }
}
