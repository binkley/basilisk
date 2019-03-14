package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import org.junit.jupiter.api.Test;

import static java.math.BigDecimal.ONE;
import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;

class IngredientTest {
    @Test
    void shouldAs() {
        final var record = new IngredientRecord(
                5L, EPOCH.plusSeconds(1L), "EGGS", ONE, 2L, 17L);
        // The types are immaterial, just that the transformation worked
        final var targetIngredient = 1;

        @SuppressWarnings("PMD") final var that
                = new UsedIngredient(record).asAny(
                (id, name, quantity, recipeId, chefId) -> {
                    assertThat(id).isEqualTo(record.getId());
                    assertThat(name).isEqualTo(record.getName());
                    assertThat(quantity).isEqualTo(record.getQuantity());
                    assertThat(recipeId).isEqualTo(record.getRecipeId());
                    assertThat(chefId).isEqualTo(record.getChefId());
                    return targetIngredient;
                });

        assertThat(that).isSameAs(targetIngredient);
    }
}
