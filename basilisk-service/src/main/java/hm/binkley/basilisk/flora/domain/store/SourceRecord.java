package hm.binkley.basilisk.flora.domain.store;

import hm.binkley.basilisk.store.StandardRecord;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@EqualsAndHashCode(callSuper = false)
@Table("FLORA.SOURCE")
@ToString(callSuper = true)
public final class SourceRecord
        extends StandardRecord<SourceRecord, SourceRepository, SourceStore> {
    @Getter
    String name;

    public SourceRecord(final Long id, final Instant receivedAt,
            final String name) {
        super(() -> new SourceRecord(id, receivedAt, name), id, receivedAt);
        this.name = name;
    }

    public static SourceRecord unsaved(final String name) {
        return new SourceRecord(null, null, name);
    }
}
