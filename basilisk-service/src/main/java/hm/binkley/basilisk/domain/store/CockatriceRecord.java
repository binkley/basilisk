package hm.binkley.basilisk.domain.store;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.Instant;

@EqualsAndHashCode(exclude = {"id", "receivedAt", "store"})
@Table("BASILISK.COCKATRICE")
@ToString
public final class CockatriceRecord {
    @Id
    @Getter
    Long id;
    @Getter
    Instant receivedAt;
    @Getter
    BigDecimal beakSize;
    @Transient
    CockatriceStore store;

    public CockatriceRecord(final Long id, final Instant receivedAt,
            final BigDecimal beakSize) {
        this.id = id;
        this.receivedAt = receivedAt;
        this.beakSize = beakSize;
    }

    public static CockatriceRecord createRaw(final BigDecimal beakSize) {
        return new CockatriceRecord(null, null, beakSize);
    }

    public CockatriceRecord save() {
        return store.save(this);
    }
}
