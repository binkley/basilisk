package hm.binkley.basilisk.store.x;

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
    @Transient
    public KindStore store;

    public static KindRecord unsaved(
            final String code, final BigDecimal coolness) {
        checkCode(code);
        final var unsaved = new KindRecord();
        unsaved.code = code;
        unsaved.coolness = coolness;
        return unsaved;
    }

    private static void checkCode(final String code) { requireNonNull(code);}

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
            near.save();
            final var ref = new NearRef();
            ref.nearCode = near.code;
            return ref;
        }
    }
}
