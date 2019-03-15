package hm.binkley.basilisk.flora.domain.store;

import hm.binkley.basilisk.store.StandardRecord;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@EqualsAndHashCode(callSuper = false)
@Table("FLORA.CHEF")
@ToString(callSuper = true)
public final class ChefRecord
        extends StandardRecord<ChefRecord, ChefRepository, ChefStore> {
    @Getter
    public String name;

    public ChefRecord(final Long id, final Instant receivedAt,
            final String name) {
        super(() -> new ChefRecord(id, receivedAt, name), id, receivedAt);
        this.name = name;
    }

    public static ChefRecord raw(final String name) {
        return new ChefRecord(null, null, name);
    }
}
