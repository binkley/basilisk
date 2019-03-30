package hm.binkley.basilisk.flora.recipe;

import org.junit.jupiter.api.Test;

import static hm.binkley.basilisk.flora.FloraFixtures.savedRecipeRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.savedUsedIngredientRecord;
import static org.assertj.core.api.Assertions.assertThat;

class RecipeTest {
    @Test
    void shouldAs() {
        final var ingredientRecord = savedUsedIngredientRecord();
        final var record = savedRecipeRecord()
                .addUnusedIngredient(ingredientRecord);
        // The types are immaterial, just that the transformation worked
        final var targetRecipe = 1;
        final var targetIngredient = "2";

        @SuppressWarnings("PMD") final var that
                = new Recipe(record).as(
                (id, code, name, cid, ingredients) -> {
                    assertThat(id).isEqualTo(record.getId());
                    assertThat(code).isEqualTo(record.getCode());
                    assertThat(name).isEqualTo(record.getName());
                    assertThat(cid).isEqualTo(record.getChefId());
                    assertThat(ingredients).containsExactly(targetIngredient);
                    return targetRecipe;
                }, (id, code, sourceId, name, quantity, rid, cid) -> {
                    assertThat(id).isEqualTo(ingredientRecord.getId());
                    assertThat(code).isEqualTo(ingredientRecord.getCode());
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
