package hm.binkley.basilisk.x.side.store;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;

import static java.util.Objects.requireNonNull;

@EqualsAndHashCode(exclude = "store")
@Table("X.SIDE")
@ToString(exclude = "store")
public class SideRecord {
    @Id
    public @NotNull String code;
    public long sequenceNumber;
    @Transient
    public SideStore store;

    public static SideRecord unsaved(
            final String code, final long sequenceNumber) {
        checkCode(code);
        checkSequenceNumber(sequenceNumber);
        final var unsaved = new SideRecord();
        unsaved.code = code;
        unsaved.sequenceNumber = sequenceNumber;
        return unsaved;
    }

    private static void checkCode(final String code) { requireNonNull(code);}

    private static void checkSequenceNumber(final long sequenceNumber) {
        if (0 > sequenceNumber) throw new IllegalArgumentException();
    }

    public SideRecord save() {
        return store.save(this);
    }

    public void delete() { store.delete(this); }
}
