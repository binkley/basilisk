package hm.binkley.basilisk.flora.domain.store;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@EqualsAndHashCode(exclude = {"id", "receivedAt", "store"})
@Table("FLORA.CHEF")
@ToString
public final class ChefRecord {
    @Id
    @Getter
    Long id;
    @Getter
    Instant receivedAt;
    @Getter
    String code;
    @Getter
    String name;
    @Transient
    ChefStore store;

    public ChefRecord(final Long id, final Instant receivedAt,
            final String code, final String name) {
        this.id = id;
        this.receivedAt = receivedAt;
        this.code = code;
        this.name = name;
    }

    public static ChefRecord raw(final String code, final String name) {
        return new ChefRecord(null, null, code, name);
    }

    public ChefRecord save() {
        return store.save(this);
    }
}
