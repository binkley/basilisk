package hm.binkley.basilisk.flora.ingredient;

import hm.binkley.basilisk.flora.source.Source;
import hm.binkley.basilisk.flora.source.Sources;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static hm.binkley.basilisk.flora.FloraFixtures.savedSourceRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.savedUsedIngredientRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class IngredientTest {
    @Mock
    private final Sources sources;

    @Test
    void shouldGetSourceAsDomain() {
        final var record = savedUsedIngredientRecord();
        final var ingredient = new UnusedIngredient(record, sources);
        when(sources.byId(record.getSourceId()))
                .thenReturn(Optional.of(
                        new Source(savedSourceRecord(), null)));

        final var source = ingredient.getSource().orElseThrow();

        assertThat(source.getId()).isEqualTo(record.getSourceId());
    }

    @Test
    void shouldAs() {
        final var record = savedUsedIngredientRecord();
        // The types are immaterial, just that the transformation worked
        final var targetIngredient = 1;

        @SuppressWarnings("PMD") final var that
                = new UsedIngredient(record, null).asAny(
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
