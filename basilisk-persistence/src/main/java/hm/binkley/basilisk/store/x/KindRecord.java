package hm.binkley.basilisk.store.x;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

import static java.util.Objects.requireNonNull;

@EqualsAndHashCode(exclude = "store")
@Table("X.KIND")
@ToString(exclude = "store")
public class KindRecord {
    @Id
    public @NotNull String code;
    public BigDecimal coolness;
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
}
