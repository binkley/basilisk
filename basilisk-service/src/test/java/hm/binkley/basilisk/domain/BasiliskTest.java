package hm.binkley.basilisk.domain;

import hm.binkley.basilisk.domain.store.BasiliskRecord;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;

class BasiliskTest {
    @Test
    void shouldAs() {
        final var record = new BasiliskRecord(
                3L, EPOCH, "FOO", Instant.ofEpochSecond(1L));

        @SuppressWarnings("PMD")
        final var that = new Basilisk(record).as(
                (id, receivedAt, word, at) -> {
                    assertThat(id).isEqualTo(record.getId());
                    assertThat(receivedAt).isEqualTo(record.getReceivedAt());
                    assertThat(word).isEqualTo(record.getWord());
                    assertThat(at).isEqualTo(record.getAt());
                    return this;
                });

        assertThat(that).isSameAs(this);
    }
}
