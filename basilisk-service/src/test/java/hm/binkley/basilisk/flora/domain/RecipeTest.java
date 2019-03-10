package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import hm.binkley.basilisk.flora.domain.store.RecipeRecord;
import org.junit.jupiter.api.Test;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;

class RecipeTest {
    @Test
    void shouldAs() {
        final var ingredientRecord = new IngredientRecord(
                5L, EPOCH.plusSeconds(1L), "EGGS");
        final var record = new RecipeRecord(
                3L, EPOCH, "SOUFFLE")
                .add(ingredientRecord);

        // The types are immaterial, just that the transformation worked
        final var targetRecipe = 1;
        final var targetIngredient = "2";

        @SuppressWarnings("PMD") final var that = new Recipe(record).as(
                (id, receivedAt, name, ingredients) -> {
                    assertThat(id).isEqualTo(record.getId());
                    assertThat(receivedAt).isEqualTo(record.getReceivedAt());
                    assertThat(name).isEqualTo(record.getName());
                    assertThat(ingredients).containsExactly(targetIngredient);
                    return targetRecipe;
                }, (id, receivedAt, name) -> {
                    assertThat(id).isEqualTo(ingredientRecord.getId());
                    assertThat(receivedAt)
                            .isEqualTo(ingredientRecord.getReceivedAt());
                    assertThat(name)
                            .isEqualTo(ingredientRecord.getName());
                    return targetIngredient;
                });

        assertThat(that).isSameAs(targetRecipe);
    }
}
