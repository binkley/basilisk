package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.RecipeRecord;
import org.junit.jupiter.api.Test;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.savedUsedIngredientRecord;
import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;

class RecipeTest {
    @Test
    void shouldAs() {
        final var recipeId = 3L;
        final var ingredientRecord = savedUsedIngredientRecord();
        final var record = new RecipeRecord(
                recipeId, EPOCH, "SOUFFLE", CHEF_ID)
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
                    assertThat(ingredients).containsExactly(targetIngredient);
                    return targetRecipe;
                }, (id, sourceId, name, quantity, rid, cid) -> {
                    assertThat(id).isEqualTo(ingredientRecord.getId());
                    assertThat(sourceId)
                            .isEqualTo(ingredientRecord.getSourceId());
                    assertThat(name).isEqualTo(ingredientRecord.getName());
                    assertThat(quantity)
                            .isEqualTo(ingredientRecord.getQuantity());
                    assertThat(rid).isEqualTo(ingredientRecord.getRecipeId());
                    assertThat(cid).isEqualTo(ingredientRecord.getChefId());
                    return targetIngredient;
                });

        assertThat(that).isSameAs(targetRecipe);
    }
}
