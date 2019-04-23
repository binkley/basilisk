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
    @Transient
    public SideStore store;

    public static SideRecord unsaved(final String code) {
        checkCode(code);
        final var unsaved = new SideRecord();
        unsaved.code = code;
        return unsaved;
    }

    private static void checkCode(final String code) { requireNonNull(code);}

    public SideRecord save() {
        return store.save(this);
    }

    public void delete() { store.delete(this); }
}
