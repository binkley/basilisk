package hm.binkley.basilisk.domain.store;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

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
    @Transient
    BasiliskStore store;

    public BasiliskRecord(final Long id, final Instant receivedAt,
            final String word,
            final Instant at) {
        this.id = id;
        this.receivedAt = receivedAt;
        this.word = word;
        this.at = at;
    }

    static BasiliskRecord create(final String word, final Instant at) {
        return new BasiliskRecord(null, null, word, at);
    }

    public BasiliskRecord save() {
        return store.save(this);
    }
}
