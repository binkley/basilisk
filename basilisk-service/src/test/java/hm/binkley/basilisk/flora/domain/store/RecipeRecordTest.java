package hm.binkley.basilisk.flora.domain.store;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.savedRecipeRecord;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.unsavedRecipeRecord;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.unsavedUnusedIngredientRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeRecordTest {
    @Mock
    private RecipeStore store;

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

    @Test
    void shouldClone() {
        final var unsaved = unsavedRecipeRecord();

        assertThat(unsaved.clone()).isEqualTo(unsaved);
    }
}
