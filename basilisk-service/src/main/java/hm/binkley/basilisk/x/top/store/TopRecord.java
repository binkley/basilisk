package hm.binkley.basilisk.x.top.store;

import hm.binkley.basilisk.x.middle.store.MiddleRecord;
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
@Table("X.TOP")
@ToString(exclude = "store")
public class TopRecord {
    @Id
    public @NotNull String code;
    public String name;
    @Column("top_code")
    public Set<MiddleRef> middles = new LinkedHashSet<>();
    public @NotNull String sideCode;
    public boolean planned;
    @Column("top_code")
    public Set<NearRef> nears = new LinkedHashSet<>();
    @Transient
    public TopStore store;

    public static TopRecord unsaved(final String code, final String name,
            final SideRecord side, final boolean planned) {
        checkCode(code);
        check(side);
        final var unsaved = new TopRecord();
        unsaved.code = code;
        unsaved.name = name;
        unsaved.sideCode = side.save().code;
        unsaved.planned = planned;
        return unsaved;
    }

    private static void checkCode(final String code) { requireNonNull(code);}

    private static void check(final SideRecord side) {
        requireNonNull(side);
    }

    public TopRecord addMiddle(final MiddleRecord middle) {
        check(middle);
        final var ref = MiddleRef.of(middle);
        if (!middles.add(ref))
            throw new IllegalStateException("Duplicate: " + middle);
        return this;
    }

    public TopRecord removeMiddle(final MiddleRecord middle) {
        check(middle);
        final var ref = MiddleRef.of(middle);
        if (!middles.remove(ref))
            throw new NoSuchElementException("Absent: " + middle);
        return this;
    }

    public TopRecord addNear(final NearRecord near) {
        check(near);
        final var ref = NearRef.of(near);
        if (!nears.add(ref))
            throw new IllegalStateException("Duplicate: " + near);
        return this;
    }

    public TopRecord removeNear(final NearRecord near) {
        check(near);
        final var ref = NearRef.of(near);
        if (!nears.remove(ref))
            throw new NoSuchElementException("Absent: " + near);
        return this;
    }

    public boolean hasNears() { return !nears.isEmpty(); }

    public TopRecord save() { return store.save(this); }

    public void delete() { store.delete(this); }

    private void check(final MiddleRecord middle) {
        requireNonNull(middle);
    }

    private void check(final NearRecord near) {
        requireNonNull(near);
    }

    @EqualsAndHashCode
    @Table("X.TOP_MIDDLE")
    @ToString
    public static class MiddleRef {
        public String middleCode;

        public static MiddleRef of(final MiddleRecord middle) {
            final var ref = new MiddleRef();
            ref.middleCode = middle.save().code;
            return ref;
        }
    }

    @EqualsAndHashCode
    @Table("X.TOP_NEAR")
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
