package hm.binkley.basilisk.domain;

import hm.binkley.basilisk.domain.store.CockatriceRecord;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.Instant;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public class Cockatrice {
    private final CockatriceRecord record;

    public Long getId() { return record.getId(); }

    public Instant getReceivedAt() { return record.getReceivedAt(); }

    public BigDecimal getBeakSize() { return record.getBeakSize(); }

    public <T> T as(final As<T> as) {
        return as.from(record.getId(), record.getReceivedAt(),
                record.getBeakSize());
    }

    public interface As<T> {
        T from(final Long id, final Instant receivedAt,
                final BigDecimal beakSize);
    }
}
