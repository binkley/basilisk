package hm.binkley.basilisk.store.x;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.LinkedHashSet;
import java.util.Set;

@EqualsAndHashCode(exclude = "id")
@Table("X.TOP")
@ToString
public class TopRecord {
    @Id
    public Long id;
    public String name;
    @Column("top_id")
    public Set<MiddleRef> middles = new LinkedHashSet<>();
    @Transient
    public TopRepository repository;

    public static TopRecord unsaved(final String name,
            final TopRepository repository) {
        final var unsaved = new TopRecord();
        unsaved.name = name;
        unsaved.repository = repository;
        return unsaved;
    }

    public TopRecord save() {
        final var saved = repository.save(this);
        saved.repository = repository;
        return saved;
    }

    public TopRecord refresh() {
        final var refreshed = repository.findById(id).orElseThrow();
        refreshed.repository = repository;
        return refreshed;
    }

    public void delete() {
        repository.delete(this);
    }

    public TopRecord add(final MiddleRecord middle) {
        if (null == middle.id)
            middle.save();
        final var ref = new MiddleRef();
        ref.middleId = middle.id;
        if (!middles.add(ref))
            throw new IllegalStateException("Duplicate: " + middle);
        return this;
    }

    public TopRecord remove(final MiddleRecord middle) {
        final var ref = new MiddleRef();
        ref.middleId = middle.id;
        if (!middles.remove(ref))
            throw new IllegalStateException("Absent: " + middle);
        return this;
    }

    @EqualsAndHashCode
    @Table("X.TOP_MIDDLE")
    @ToString
    public static class MiddleRef {
        public Long middleId;
    }
}
