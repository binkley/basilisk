package hm.binkley.basilisk.basilisk.domain.store;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

@EqualsAndHashCode(exclude = {"id", "receivedAt", "store"})
@Table("BASILISK.BASILISK")
@ToString
public final class BasiliskRecord {
    @Id
    @Getter
    Long id;
    @Getter
    Instant receivedAt;
    @Getter
    String word;
    @Getter
    Instant at;
    @Column("basilisk_id")
    @Getter
    Set<CockatriceRecord> cocatrices = new LinkedHashSet<>();
    @Transient
    BasiliskStore store;

    public BasiliskRecord(final Long id, final Instant receivedAt,
            final String word, final Instant at) {
        this.id = id;
        this.receivedAt = receivedAt;
        this.word = word;
        this.at = at;
    }

    public static BasiliskRecord raw(final String word, final Instant at) {
        return new BasiliskRecord(null, null, word, at);
    }

    public BasiliskRecord add(final CockatriceRecord cockatrice) {
        cocatrices.add(cockatrice);
        return this;
    }

    public BasiliskRecord addAll(final Stream<CockatriceRecord> cockatrices) {
        cockatrices.forEach(this::add);
        return this;
    }

    public BasiliskRecord save() {
        return store.save(this);
    }
}
