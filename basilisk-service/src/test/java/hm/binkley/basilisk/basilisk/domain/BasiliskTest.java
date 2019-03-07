package hm.binkley.basilisk.basilisk.domain;

import hm.binkley.basilisk.basilisk.domain.store.BasiliskRecord;
import hm.binkley.basilisk.basilisk.domain.store.CockatriceRecord;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static java.math.BigDecimal.TEN;
import static java.time.Instant.EPOCH;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class BasiliskTest {
    @Test
    void shouldAs() {
        final var cockatriceRecord = new CockatriceRecord(
                5L, EPOCH.plusSeconds(1L), TEN);
        final var record = new BasiliskRecord(
                3L, EPOCH, "FOO", Instant.ofEpochSecond(1L))
                .add(cockatriceRecord);

        @SuppressWarnings("PMD") final var that = new Basilisk(record).as(
                (id, receivedAt, word, at, cockatrices) -> {
                    assertThat(id).isEqualTo(record.getId());
                    assertThat(receivedAt).isEqualTo(record.getReceivedAt());
                    assertThat(word).isEqualTo(record.getWord());
                    assertThat(at).isEqualTo(record.getAt());
                    final var c = cockatrices.collect(toList());
                    assertThat(c).hasSize(1);
                    assertThat(c).first().isSameAs(this);
                    return this;
                }, (id, receivedAt, beakSize) -> {
                    assertThat(id).isEqualTo(cockatriceRecord.getId());
                    assertThat(receivedAt)
                            .isEqualTo(cockatriceRecord.getReceivedAt());
                    assertThat(beakSize)
                            .isEqualTo(cockatriceRecord.getBeakSize());
                    return this;
                });

        assertThat(that).isSameAs(this);
    }
}
