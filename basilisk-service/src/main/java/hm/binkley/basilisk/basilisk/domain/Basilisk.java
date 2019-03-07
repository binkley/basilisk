package hm.binkley.basilisk.basilisk.domain;

import hm.binkley.basilisk.basilisk.domain.store.BasiliskRecord;
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

    public <T, U> T as(final As<T, U> asBasilisk,
            final Cockatrice.As<U> asCockatrice) {
        return asBasilisk.from(record.getId(), record.getReceivedAt(),
                record.getWord(), record.getAt(),
                cockatrices().map(it -> asCockatrice.from(
                        it.getId(),
                        it.getReceivedAt(),
                        it.getBeakSize())));
    }

    public interface As<T, U> {
        T from(final Long id, final Instant receivedAt,
                final String word, final Instant at,
                final Stream<U> cockatrices);
    }
}
