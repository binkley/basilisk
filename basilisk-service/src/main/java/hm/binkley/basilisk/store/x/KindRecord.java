package hm.binkley.basilisk.store.x;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@EqualsAndHashCode(exclude = {"id", "repository"})
@Table("X.KIND")
@ToString
public class KindRecord {
    @Id
    public Long id;
    public BigDecimal coolness;
    @Transient
    public KindRepository repository;

    public static KindRecord unsaved(final BigDecimal coolness,
            final KindRepository repository) {
        final var unsaved = new KindRecord();
        unsaved.coolness = coolness;
        unsaved.repository = repository;
        return unsaved;
    }

    public KindRecord save() {
        final var saved = repository.save(this);
        saved.repository = repository;
        return saved;
    }

    public KindRecord refresh() {
        final var refreshed = repository.findById(id).orElseThrow();
        refreshed.repository = repository;
        return refreshed;
    }

    public void delete() {
        repository.delete(this);
    }
}
