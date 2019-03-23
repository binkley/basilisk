package hm.binkley.basilisk.flora.chef.store;

import hm.binkley.basilisk.store.StandardRecord;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@EqualsAndHashCode(callSuper = false)
@Table("FLORA.CHEF")
@ToString(callSuper = true)
public final class ChefRecord
        extends StandardRecord<ChefRecord, ChefRepository, ChefStore> {
    @Getter
    public @NotNull String code;
    @Getter
    public @NotNull String name;

    public ChefRecord(final Long id, final Instant receivedAt,
            final String code, final String name) {
        super(() -> new ChefRecord(id, receivedAt, code, name),
                id, receivedAt, code);
        this.code = code;
        this.name = name;
    }

    public static ChefRecord unsaved(final String code, final String name) {
        return new ChefRecord(null, null, code, name);
    }
}
