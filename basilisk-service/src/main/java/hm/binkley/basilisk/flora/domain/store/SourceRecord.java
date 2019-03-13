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
@Table("FLORA.SOURCE")
@ToString
public final class SourceRecord {
    @Id
    @Getter
    Long id;
    @CreatedDate
    @Getter
    Instant receivedAt;
    @Getter
    String name;
    @Transient
    SourceStore store;

    public SourceRecord(final Long id, final Instant receivedAt,
            final String name) {
        this.id = id;
        this.receivedAt = receivedAt;
        this.name = name;
    }

    public static SourceRecord raw(final String name) {
        return new SourceRecord(null, null, name);
    }

    public SourceRecord save() {
        return store.save(this);
    }
}
