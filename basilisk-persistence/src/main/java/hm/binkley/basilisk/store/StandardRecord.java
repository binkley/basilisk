package hm.binkley.basilisk.store;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;

import java.time.Instant;
import java.util.function.Function;

@EqualsAndHashCode(exclude = "store")
@SuppressWarnings("PMD.AbstractClassWithoutAbstractMethod")
@ToString(exclude = "store")
public abstract class StandardRecord<T extends StandardRecord<T, R, S>,
        R extends StandardRepository<T, R, S>,
        S extends StandardStore<T, R, S>> {
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

    public StandardRecord(final Long id, final Instant receivedAt,
            final String code) {
        this.id = id;
        this.receivedAt = receivedAt;
        this.code = code;
    }

    public <F> F asRef(final Function<Long, F> ctor) {
        return ctor.apply(idOrSave());
    }

    void become(final T other) {
        id = other.getId();
        receivedAt = other.getReceivedAt();
    }

    private Long idOrSave() {
        final var id = getId();
        return null == id
                ? save().getId()
                : id;
    }

    @SuppressWarnings("unchecked")
    public final T save() {
        return store.save((T) this);
    }

    @SuppressWarnings("unchecked")
    public final T delete() {
        return store.delete((T) this);
    }
}
