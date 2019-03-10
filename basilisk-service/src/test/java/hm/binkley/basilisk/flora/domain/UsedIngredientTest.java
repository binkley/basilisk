package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import org.junit.jupiter.api.Test;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;

class UsedIngredientTest {
    @Test
    void shouldAs() {
        final var record = new IngredientRecord(
                5L, EPOCH.plusSeconds(1L), "EGGS", 2L);
        // The types are immaterial, just that the transformation worked
        final var targetIngredient = 1;

        @SuppressWarnings("PMD") final var that
                = new UsedIngredient(record).asUsed(
                (id, receivedAt, name, recipeId) -> {
                    assertThat(id).isEqualTo(record.getId());
                    assertThat(receivedAt)
                            .isEqualTo(record.getReceivedAt());
                    assertThat(name).isEqualTo(record.getName());
                    assertThat(recipeId)
                            .isEqualTo(record.getRecipeId());
                    return targetIngredient;
                });

        assertThat(that).isSameAs(targetIngredient);
    }
}
