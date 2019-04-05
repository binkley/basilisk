package hm.binkley.basilisk.store.x;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;

import static java.util.Objects.requireNonNull;

@EqualsAndHashCode(exclude = "store")
@Table("X.NEAR")
@ToString(exclude = "store")
public class NearRecord {
    @Id
    public @NotNull String code;
    @Transient
    public NearStore store;

    public static NearRecord unsaved(final String code) {
        checkCode(code);
        final var unsaved = new NearRecord();
        unsaved.code = code;
        return unsaved;
    }

    private static void checkCode(final String code) { requireNonNull(code);}

    public NearRecord save() {
        return store.save(this);
    }

    public void delete() { store.delete(this); }
}
