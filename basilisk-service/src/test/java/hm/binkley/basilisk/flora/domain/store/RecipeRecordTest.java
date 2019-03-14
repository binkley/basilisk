package hm.binkley.basilisk.flora.domain.store;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Set;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.unsavedIngredientRecord;
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
        final var ingredientRecord = unsavedIngredientRecord();
        final var record = RecipeRecord.raw("SOUFFLE", CHEF_ID)
                .addAll(Stream.of(ingredientRecord));

        assertThat(record.ingredients).isEqualTo(Set.of(ingredientRecord));
        // TODO: Auto-save when adding ingredients?
    }

    @Test
    void shouldSaveWithoutIngredients() {
        final var unsaved = RecipeRecord.raw("SOUFFLE", CHEF_ID);
        unsaved.store = store;
        final var saved = new RecipeRecord(
                3L, EPOCH, unsaved.getName(), CHEF_ID);
        saved.store = store;
        when(store.save(unsaved))
                .thenReturn(saved);

        assertThat(unsaved.save()).isEqualTo(saved);

        verify(store).save(unsaved);
        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldSaveWithIngredients() {
        final var unsaved = RecipeRecord.raw("SOUFFLE", CHEF_ID);
        unsaved.store = store;
        final var unsavedIngredient = unsavedIngredientRecord();
        unsaved.ingredients.add(unsavedIngredient);
        final var saved = new RecipeRecord(
                3L, EPOCH, unsaved.getName(), CHEF_ID);
        saved.store = store;
        saved.ingredients.add(unsavedIngredient);
        when(store.save(unsaved))
                .thenReturn(saved);

        assertThat(unsaved.save()).isEqualTo(saved);

        verify(store).save(unsaved);
        verifyNoMoreInteractions(store);
    }
}
