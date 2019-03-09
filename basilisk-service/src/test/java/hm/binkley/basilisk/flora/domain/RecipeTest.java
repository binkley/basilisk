package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.RecipeRecord;
import org.junit.jupiter.api.Test;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;

class RecipeTest {
    @Test
    void shouldAs() {
        final var record = new RecipeRecord(
                5L, EPOCH.plusSeconds(1L), "SOUFFLE");

        @SuppressWarnings("PMD") final var that = new Recipe(record).as(
                (id, receivedAt, name) -> {
                    assertThat(id).isEqualTo(record.getId());
                    assertThat(receivedAt).isEqualTo(record.getReceivedAt());
                    assertThat(name).isEqualTo(record.getName());
                    return this;
                });

        assertThat(that).isSameAs(this);
    }
}
