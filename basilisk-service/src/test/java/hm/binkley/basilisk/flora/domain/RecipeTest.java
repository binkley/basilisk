package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import hm.binkley.basilisk.flora.domain.store.RecipeRecord;
import org.junit.jupiter.api.Test;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;

class RecipeTest {
    @Test
    void shouldAs() {
        final var recipeId = 3L;
        final var chefId = 17L;
        final var ingredientRecord = new IngredientRecord(
                5L, EPOCH.plusSeconds(1L), "EGGS", recipeId, chefId);
        final var record = new RecipeRecord(
                recipeId, EPOCH, "SOUFFLE", chefId)
                .add(ingredientRecord);
        // The types are immaterial, just that the transformation worked
        final var targetRecipe = 1;
        final var targetIngredient = "2";

        @SuppressWarnings("PMD") final var that
                = new Recipe(record).as(
                (id, name, cid, ingredients) -> {
                    assertThat(id).isEqualTo(record.getId());
                    assertThat(name).isEqualTo(record.getName());
                    assertThat(cid).isEqualTo(record.getChefId());
                    assertThat(ingredients).containsOnly(targetIngredient);
                    return targetRecipe;
                }, (id, name, rid, cid) -> {
                    assertThat(id).isEqualTo(ingredientRecord.getId());
                    assertThat(name).isEqualTo(ingredientRecord.getName());
                    assertThat(rid).isEqualTo(ingredientRecord.getRecipeId());
                    assertThat(cid).isEqualTo(ingredientRecord.getChefId());
                    return targetIngredient;
                });

        assertThat(that).isSameAs(targetRecipe);
    }
}
