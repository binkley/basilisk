package hm.binkley.basilisk.flora.domain.store;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
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
    @CreatedDate
    @Getter
    Instant receivedAt;
    @Getter
    String name;
    @Transient
    ChefStore store;

    public ChefRecord(final Long id, final Instant receivedAt,
            final String name) {
        this.id = id;
        this.receivedAt = receivedAt;
        this.name = name;
    }

    public static ChefRecord raw(final String name) {
        return new ChefRecord(null, null, name);
    }

    public ChefRecord save() {
        return store.save(this);
    }
}
