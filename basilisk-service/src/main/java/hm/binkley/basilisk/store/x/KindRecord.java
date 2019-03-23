package hm.binkley.basilisk.store.x;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@EqualsAndHashCode(exclude = {"id", "store"})
@Table("X.KIND")
@ToString(exclude = "store")
public class KindRecord {
    @Id
    public Long id;
    public BigDecimal coolness;
    @Transient
    public KindStore store;

    public static KindRecord unsaved(final BigDecimal coolness) {
        final var unsaved = new KindRecord();
        unsaved.coolness = coolness;
        return unsaved;
    }

    public KindRecord save() {
        return store.save(this);
    }

    public void delete() { store.delete(this); }
}
