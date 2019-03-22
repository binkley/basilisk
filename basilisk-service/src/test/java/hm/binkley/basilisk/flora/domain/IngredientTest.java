package hm.binkley.basilisk.flora.domain;

import org.junit.jupiter.api.Test;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.savedUsedIngredientRecord;
import static org.assertj.core.api.Assertions.assertThat;

class IngredientTest {
    @Test
    void shouldAs() {
        final var record = savedUsedIngredientRecord();
        // The types are immaterial, just that the transformation worked
        final var targetIngredient = 1;

        @SuppressWarnings("PMD") final var that
                = new UsedIngredient(record).asAny(
                (id, code, sourceId, name, quantity, recipeId, chefId) -> {
                    assertThat(id).isEqualTo(record.getId());
                    assertThat(code).isEqualTo(record.getCode());
                    assertThat(sourceId).isEqualTo(record.getSourceId());
                    assertThat(name).isEqualTo(record.getName());
                    assertThat(quantity).isEqualTo(record.getQuantity());
                    assertThat(recipeId).isEqualTo(record.getRecipeId());
                    assertThat(chefId).isEqualTo(record.getChefId());
                    return targetIngredient;
                });

        assertThat(that).isSameAs(targetIngredient);
    }
}
