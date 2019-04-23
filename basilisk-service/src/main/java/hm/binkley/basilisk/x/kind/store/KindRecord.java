package hm.binkley.basilisk.x.kind.store;

import hm.binkley.basilisk.x.near.store.NearRecord;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static java.util.Objects.requireNonNull;

@EqualsAndHashCode(exclude = "store")
@Table("X.KIND")
@ToString(exclude = "store")
public class KindRecord {
    @Id
    public @NotNull String code;
    public BigDecimal coolness;
    @Column("kind_code")
    public Set<NearRef> nears = new LinkedHashSet<>();
    public long sequenceNumber;
    @Transient
    public KindStore store;

    public static KindRecord unsaved(
            final String code, final BigDecimal coolness,
            final long sequenceNumber) {
        checkCode(code);
        checkSequenceNumber(sequenceNumber);
        final var unsaved = new KindRecord();
        unsaved.code = code;
        unsaved.coolness = coolness;
        unsaved.sequenceNumber = sequenceNumber;
        return unsaved;
    }

    private static void checkCode(final String code) { requireNonNull(code);}

    private static void checkSequenceNumber(final long sequenceNumber) {
        if (0 > sequenceNumber) throw new IllegalArgumentException();
    }

    public KindRecord save() {
        return store.save(this);
    }

    public void delete() { store.delete(this); }

    public KindRecord addNear(final NearRecord near) {
        check(near);
        final var ref = NearRef.of(near);
        if (!nears.add(ref))
            throw new IllegalStateException("Duplicate: " + near);
        return this;
    }

    public KindRecord removeNear(final NearRecord near) {
        check(near);
        final var ref = NearRef.of(near);
        if (!nears.remove(ref))
            throw new NoSuchElementException("Absent: " + near);
        return this;
    }

    public boolean hasNears() {
        return !nears.isEmpty();
    }

    private void check(final NearRecord near) {
        requireNonNull(near);
    }

    @EqualsAndHashCode
    @Table("X.KIND_NEAR")
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
