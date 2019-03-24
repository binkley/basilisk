package hm.binkley.basilisk.flora.recipe.store;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.FloraFixtures.savedRecipeRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedRecipeRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedUnusedIngredientRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class RecipeRecordTest {
    @Mock
    private final RecipeStore store;

    @Test
    void shouldAddSomeIngredients() {
        final var ingredientRecord = unsavedUnusedIngredientRecord();
        final var record = unsavedRecipeRecord()
                .addAll(Stream.of(ingredientRecord));

        assertThat(record.ingredients).isEqualTo(Set.of(ingredientRecord));
    }

    @Test
    void shouldSaveWithoutIngredients() {
        final var unsaved = unsavedRecipeRecord();
        unsaved.store = store;
        final var saved = savedRecipeRecord();
        saved.store = store;
        when(store.save(unsaved))
                .thenReturn(saved);

        assertThat(unsaved.save()).isEqualTo(saved);

        verify(store).save(unsaved);
        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldSaveWithIngredients() {
        final var unsaved = unsavedRecipeRecord();
        unsaved.store = store;
        final var unsavedIngredient = unsavedUnusedIngredientRecord();
        unsaved.ingredients.add(unsavedIngredient);
        final var saved = savedRecipeRecord();
        saved.store = store;
        saved.ingredients.add(unsavedIngredient);
        when(store.save(unsaved))
                .thenReturn(saved);

        assertThat(unsaved.save()).isEqualTo(saved);

        verify(store).save(unsaved);
        verifyNoMoreInteractions(store);
    }
}
