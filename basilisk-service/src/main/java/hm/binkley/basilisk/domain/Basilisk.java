package hm.binkley.basilisk.domain;

import hm.binkley.basilisk.domain.store.BasiliskRecord;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.stream.Stream;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public class Basilisk {
    private final BasiliskRecord record;

    public Stream<Cockatrice> cockatrices() {
        return record.getCocatrices().stream()
                .map(Cockatrice::new);
    }

    public <T> T as(final As<T> as) {
        return as.from(record.getId(), record.getReceivedAt(),
                record.getWord(), record.getAt(), cockatrices());
    }

    public interface As<T> {
        T from(final Long id, final Instant receivedAt,
                final String word, final Instant at,
                final Stream<Cockatrice> cockatrice);
    }
}
