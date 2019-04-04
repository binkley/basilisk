package hm.binkley.basilisk.store.x;

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
    public int mid;
    @Column("middle_code")
    public Set<BottomRecord> bottoms = new LinkedHashSet<>();
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

    public MiddleRecord define(final @NotNull KindRecord kind) {
        check(kind);
        kind.save();
        kindCode = kind.code;
        return this;
    }

    public MiddleRecord add(final @NotNull BottomRecord bottom) {
        check(bottom);
        if (!bottoms.add(bottom))
            throw new IllegalStateException("Duplicate: " + bottom);
        return this;
    }

    public MiddleRecord remove(final @NotNull BottomRecord bottom) {
        check(bottom);
        if (!bottoms.remove(bottom))
            throw new NoSuchElementException("Absent: " + bottom);
        return this;
    }

    private void check(final BottomRecord bottom) {
        requireNonNull(bottom);
        if (code.equals(bottom.middleCode)
                || null == bottom.middleCode) return;

        throw new IllegalStateException(
                "Mismatched: " + bottom + "; " + this);
    }

    private void check(final KindRecord kind) {
        requireNonNull(kind);
    }
}
