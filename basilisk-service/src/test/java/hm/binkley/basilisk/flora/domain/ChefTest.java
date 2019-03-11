package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.ChefRecord;
import org.junit.jupiter.api.Test;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;

class ChefTest {
    @Test
    void shouldAs() {
        final var record = new ChefRecord(
                3L, EPOCH, "ABC", "Chef Paul");
        // The types are immaterial, just that the transformation worked
        final var targetChef = 1;

        @SuppressWarnings("PMD") final var that
                = new Chef(record).as(
                (id, receivedAt, code, name) -> {
                    assertThat(id).isEqualTo(record.getId());
                    assertThat(receivedAt).isEqualTo(record.getReceivedAt());
                    assertThat(code).isEqualTo(record.getCode());
                    assertThat(name).isEqualTo(record.getName());
                    return targetChef;
                });

        assertThat(that).isSameAs(targetChef);
    }
}
