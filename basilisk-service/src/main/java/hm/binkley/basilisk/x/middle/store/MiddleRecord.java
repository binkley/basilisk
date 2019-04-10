package hm.binkley.basilisk.x.middle.store;

import hm.binkley.basilisk.x.kind.store.KindRecord;
import hm.binkley.basilisk.x.near.store.NearRecord;
import hm.binkley.basilisk.x.side.store.SideRecord;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static java.util.Objects.requireNonNull;

@EqualsAndHashCode(exclude = "store")
@Table("X.MIDDLE")
@ToString(exclude = "store")
public class MiddleRecord {
    @Id
    public @NotNull String code;
    public String kindCode;
    public String sideCode;
    public int mid;
    @Column("middle_code")
    public Set<BottomRecord> bottoms = new LinkedHashSet<>();
    @Column("middle_code")
    public Set<NearRef> nears = new LinkedHashSet<>();
    @Transient
    public MiddleStore store;

    public static MiddleRecord unsaved(final String code, final int mid) {
        checkCode(code);
        final var unsaved = new MiddleRecord();
        unsaved.code = code;
        unsaved.mid = mid;
        return unsaved;
    }

    private static void checkCode(final String code) { requireNonNull(code);}

    public MiddleRecord save() {
        return store.save(this);
    }

    public void delete() { store.delete(this); }

    public MiddleRecord defineKind(final @NotNull KindRecord kind) {
        check(kind);
        kindCode = kind.save().code;
        return this;
    }

    @SuppressWarnings("PMD.NullAssignment")
    public MiddleRecord detachFromKind() {
        if (null == kindCode) {
            throw new IllegalStateException("Absent kind");
        }
        kindCode = null;
        return this;
    }

    public MiddleRecord attachToSide(final @NotNull SideRecord side) {
        check(side);
        sideCode = side.save().code;
        return this;
    }

    @SuppressWarnings("PMD.NullAssignment")
    public MiddleRecord detachFromSide() {
        if (null == sideCode) {
            throw new IllegalStateException("Absent side");
        }
        sideCode = null;
        return this;
    }

    public MiddleRecord addBottom(final @NotNull BottomRecord bottom) {
        check(bottom);
        if (!bottoms.add(bottom))
            throw new IllegalStateException("Duplicate: " + bottom);
        return this;
    }

    public MiddleRecord removeBottom(final @NotNull BottomRecord bottom) {
        check(bottom);
        if (!bottoms.remove(bottom))
            throw new NoSuchElementException("Absent: " + bottom);
        return this;
    }

    public MiddleRecord addNear(final NearRecord near) {
        check(near);
        final var ref = NearRef.of(near);
        if (!nears.add(ref))
            throw new IllegalStateException("Duplicate: " + near);
        return this;
    }

    public MiddleRecord removeNear(final NearRecord near) {
        check(near);
        final var ref = NearRef.of(near);
        if (!nears.remove(ref))
            throw new NoSuchElementException("Absent: " + near);
        return this;
    }

    public boolean hasNears() {
        return !nears.isEmpty();
    }

    private void check(final KindRecord kind) {
        requireNonNull(kind);
    }

    private void check(final BottomRecord bottom) {
        requireNonNull(bottom);
        if (null != bottom.middleCode)
            throw new IllegalStateException(
                    "Mismatched: " + bottom + "; " + this);
    }

    private void check(final SideRecord side) {
        requireNonNull(side);
    }

    private void check(final NearRecord near) {
        requireNonNull(near);
    }

    @EqualsAndHashCode
    @Table("X.MIDDLE_NEAR")
    @ToString
    public static class NearRef {
        public String nearCode;

        public static NearRef of(final NearRecord near) {
            final var ref = new NearRef();
            ref.nearCode = near.save().code;
            return ref;
        }
    }
}
