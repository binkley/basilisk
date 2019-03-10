package hm.binkley.basilisk.flora.domain.store;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.stream.Stream;

import static java.time.Instant.EPOCH;
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
        final var ingredientRecord = IngredientRecord.raw("EGGS");
        final var record = RecipeRecord.raw("SOUFFLE")
                .addAll(Stream.of(ingredientRecord));

        assertThat(record.ingredients).isEqualTo(Set.of(ingredientRecord));
        // TODO: Auto-save when adding ingredients?
    }

    @Test
    void shouldSaveWithoutIngredients() {
        final var unsaved = RecipeRecord.raw("SOUFFLE");
        unsaved.store = store;
        final var saved = new RecipeRecord(
                3L, EPOCH, unsaved.getName());
        saved.store = store;
        when(store.save(unsaved))
                .thenReturn(saved);

        assertThat(unsaved.save()).isEqualTo(saved);

        verify(store).save(unsaved);
        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldSaveWithIngredients() {
        final var unsaved = RecipeRecord.raw("SOUFFLE");
        unsaved.store = store;
        final var unsavedIngredient = IngredientRecord.raw("EGGS");
        unsaved.ingredients.add(unsavedIngredient);
        final var saved = new RecipeRecord(3L, EPOCH, unsaved.getName());
        saved.store = store;
        saved.ingredients.add(unsavedIngredient);
        when(store.save(unsaved))
                .thenReturn(saved);

        assertThat(unsaved.save()).isEqualTo(saved);

        verify(store).save(unsaved);
        verifyNoMoreInteractions(store);
    }
}
