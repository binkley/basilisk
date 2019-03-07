package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import org.junit.jupiter.api.Test;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;

class IngredientTest {
    @Test
    void shouldAs() {
        final var record = new IngredientRecord(
                5L, EPOCH.plusSeconds(1L), "EGG");

        @SuppressWarnings("PMD") final var that = new Ingredient(record).as(
                (id, receivedAt, name) -> {
                    assertThat(id).isEqualTo(record.getId());
                    assertThat(receivedAt).isEqualTo(record.getReceivedAt());
                    assertThat(name).isEqualTo(record.getName());
                    return this;
                });

        assertThat(that).isSameAs(this);
    }
}
