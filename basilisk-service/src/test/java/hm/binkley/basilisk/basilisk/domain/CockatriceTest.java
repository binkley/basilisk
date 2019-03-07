package hm.binkley.basilisk.basilisk.domain;

import hm.binkley.basilisk.basilisk.domain.store.CockatriceRecord;
import org.junit.jupiter.api.Test;

import static java.math.BigDecimal.TEN;
import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;

class CockatriceTest {
    @Test
    void shouldAs() {
        final var record = new CockatriceRecord(
                5L, EPOCH.plusSeconds(1L), TEN);

        @SuppressWarnings("PMD") final var that = new Cockatrice(record).as(
                (id, receivedAt, beakSize) -> {
                    assertThat(id).isEqualTo(record.getId());
                    assertThat(receivedAt).isEqualTo(record.getReceivedAt());
                    assertThat(beakSize).isEqualTo(record.getBeakSize());
                    return this;
                });

        assertThat(that).isSameAs(this);
    }
}
