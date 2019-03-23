package hm.binkley.basilisk.store.x;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

import static java.util.Objects.requireNonNull;

@EqualsAndHashCode(exclude = {"id", "repository"})
@Table("X.MIDDLE")
@ToString
public class MiddleRecord {
    @Id
    public Long id;
    public int mid;
    @Column("middle_id")
    public Set<BottomRecord> bottoms = new LinkedHashSet<>();
    @Transient
    public MiddleRepository repository;

    public static MiddleRecord unsaved(final int mid,
            final MiddleRepository repository) {
        final var unsaved = new MiddleRecord();
        unsaved.mid = mid;
        unsaved.repository = repository;
        return unsaved;
    }

    /** @todo Instead require caller to discard previous bottom objects? */
    public MiddleRecord save() {
        final var saved = repository.save(this);
        saved.repository = repository;
        bottoms.forEach(this::postSave);
        return saved;
    }

    public MiddleRecord refresh() {
        final var refreshed = repository.findById(id).orElseThrow();
        refreshed.repository = repository;
        return refreshed;
    }

    public void delete() {
        repository.delete(this);
    }

    public MiddleRecord add(final BottomRecord bottom) {
        check(bottom);
        if (!bottoms.add(bottom))
            throw new IllegalStateException("Duplicate: " + bottom);
        return this;
    }

    public MiddleRecord remove(final BottomRecord bottom) {
        check(bottom);
        if (!bottoms.remove(bottom))
            throw new IllegalStateException("Absent: " + bottom);
        return this;
    }

    private void postSave(final BottomRecord middle) {
        middle.postParentSave(this);
    }

    private void check(final BottomRecord bottom) {
        requireNonNull(bottom);
        /* OK if:
        1) Both unsaved OR both saved AND bottom saved to THIS
        1a) Duplicate check in Set will affect latter
        2) Middle saved AND bottom not saved
         */
        if (Objects.equals(id, bottom.middleId)) return;
        if (null != id && null == bottom.middleId) return;

        throw new IllegalStateException(
                "Mismatched: " + bottom + "; " + this);
    }
}
