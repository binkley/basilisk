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

@EqualsAndHashCode(exclude = {"id", "store"})
@Table("X.MIDDLE")
@ToString
public class MiddleRecord {
    @Id
    public Long id;
    public Long kindId;
    public int mid;
    @Column("middle_id")
    public Set<BottomRecord> bottoms = new LinkedHashSet<>();
    @Transient
    public MiddleStore store;

    public static MiddleRecord unsaved(final int mid) {
        final var unsaved = new MiddleRecord();
        unsaved.mid = mid;
        return unsaved;
    }

    public MiddleRecord define(final KindRecord kind) {
        if (null == kind.id)
            kind.save();
        kindId = kind.id;
        return this;
    }

    /** @todo Instead require caller to discard previous bottom objects? */
    public MiddleRecord save() {
        final var saved = store.save(this);
        bottoms.forEach(saved::postSave);
        return saved;
    }

    public MiddleRecord refresh() {
        return store.byId(id).orElseThrow();
    }

    public void delete() {
        store.delete(this);
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

    private void check(final KindRecord kind) {
        requireNonNull(kind);
    }
}
