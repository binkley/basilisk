package hm.binkley.basilisk.flora.ingredient;

import org.junit.jupiter.api.Test;

import static hm.binkley.basilisk.flora.FloraFixtures.savedUsedIngredientRecord;
import static org.assertj.core.api.Assertions.assertThat;

class UnusedIngredientTest {
    @Test
    void shouldAs() {
        final var record = savedUsedIngredientRecord();
        // The types are immaterial, just that the transformation worked
        final var targetIngredient = 1;

        @SuppressWarnings("PMD") final var that
                = new UnusedIngredient(record).asUnused(
                (id, code, sourceId, name, quantity, chefId) -> {
                    assertThat(id).isEqualTo(record.getId());
                    assertThat(code).isEqualTo(record.getCode());
                    assertThat(sourceId).isEqualTo(record.getSourceId());
                    assertThat(name).isEqualTo(record.getName());
                    assertThat(quantity).isEqualTo(record.getQuantity());
                    assertThat(chefId).isEqualTo(record.getChefId());
                    return targetIngredient;
                });

        assertThat(that).isSameAs(targetIngredient);
    }
}
