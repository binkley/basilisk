package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.ChefRecord;
import org.junit.jupiter.api.Test;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;

class ChefTest {
    @Test
    void shouldAs() {
        final var record = new ChefRecord(
                3L, EPOCH, "Chef Nancy");
        // The types are immaterial, just that the transformation worked
        final var targetChef = 1;

        @SuppressWarnings("PMD") final var that
                = new Chef(record).as(
                (id, name) -> {
                    assertThat(id).isEqualTo(record.getId());
                    assertThat(name).isEqualTo(record.getName());
                    return targetChef;
                });

        assertThat(that).isSameAs(targetChef);
    }
}
