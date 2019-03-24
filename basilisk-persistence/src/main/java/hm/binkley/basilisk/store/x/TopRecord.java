package hm.binkley.basilisk.store.x;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static java.util.Objects.requireNonNull;

@EqualsAndHashCode(exclude = {"id", "store"})
@Table("X.TOP")
@ToString(exclude = "store")
public class TopRecord {
    @Id
    public Long id;
    public String name;
    @Column("top_id")
    public Set<MiddleRef> middles = new LinkedHashSet<>();
    @Transient
    public TopStore store;

    public static TopRecord unsaved(final String name) {
        final var unsaved = new TopRecord();
        unsaved.name = name;
        return unsaved;
    }

    public TopRecord save() {
        return store.save(this);
    }

    public void delete() { store.delete(this); }

    public TopRecord add(final MiddleRecord middle) {
        check(middle);
        final var ref = MiddleRef.of(middle);
        if (!middles.add(ref))
            throw new IllegalStateException("Duplicate: " + middle);
        return this;
    }

    public TopRecord remove(final MiddleRecord middle) {
        check(middle);
        final var ref = MiddleRef.of(middle);
        if (!middles.remove(ref))
            throw new NoSuchElementException("Absent: " + middle);
        return this;
    }

    private void check(final MiddleRecord middle) {
        requireNonNull(middle);
    }

    @EqualsAndHashCode
    @Table("X.TOP_MIDDLE")
    @ToString
    public static class MiddleRef {
        public Long middleId;

        public static MiddleRef of(final MiddleRecord middle) {
            if (null == middle.id)
                middle.save();
            final var ref = new MiddleRef();
            ref.middleId = middle.id;
            return ref;
        }
    }
}
