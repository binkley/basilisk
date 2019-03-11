package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import org.junit.jupiter.api.Test;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;

class UnusedIngredientTest {
    @Test
    void shouldAs() {
        final var record = new IngredientRecord(
                5L, EPOCH.plusSeconds(1L), "EGGS", null);
        // The types are immaterial, just that the transformation worked
        final var targetIngredient = 1;

        @SuppressWarnings("PMD") final var that
                = new UnusedIngredient(record).asUnused(
                (id, name) -> {
                    assertThat(id).isEqualTo(record.getId());
                    assertThat(name).isEqualTo(record.getName());
                    return targetIngredient;
                });

        assertThat(that).isSameAs(targetIngredient);
    }
}
