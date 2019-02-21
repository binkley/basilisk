package hm.binkley.basilisk.domain;

import hm.binkley.basilisk.domain.store.BasiliskRecord;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public class Basilisk {
    private final BasiliskRecord record;

    public <T> T as(final As<T> as) {
        return as.from(record.getId(), record.getReceivedAt(),
                record.getWord(), record.getAt());
    }

    public interface As<T> {
        T from(final Long id, final Instant receivedAt, final String word,
                final Instant at);
    }
}
