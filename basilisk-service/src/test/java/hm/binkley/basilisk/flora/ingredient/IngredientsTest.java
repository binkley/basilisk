package hm.binkley.basilisk.flora.ingredient;

import hm.binkley.basilisk.flora.ingredient.store.IngredientStore;
import hm.binkley.basilisk.flora.source.Sources;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.FloraFixtures.savedUnusedIngredientRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.savedUsedIngredientRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedUnusedIngredientRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class IngredientsTest {
    @Mock
    private final IngredientStore store;
    @Mock
    private final Sources sources;

    private Ingredients ingredients;

    @BeforeEach
    void setUp() {
        ingredients = new Ingredients(store, sources);
    }

    @Test
    void shouldCreateUnsaved() {
        final var record = unsavedUnusedIngredientRecord();
        when(store.unsaved(
                record.getCode(), record.getSourceId(), record.getName(),
                record.getQuantity(), record.getChefId()))
                .thenReturn(record);

        final var unsaved = ingredients.unsaved(
                record.getCode(), record.getSourceId(), record.getName(),
                record.getQuantity(), record.getChefId());

        assertThat(unsaved).isEqualTo(new UnusedIngredient(record, sources));

        verifyNoMoreInteractions(store, sources);
    }

    @Test
    void shouldFindUnusedById() {
        final var record = savedUnusedIngredientRecord();
        when(store.byId(record.getId()))
                .thenReturn(Optional.of(record));

        final var found = ingredients
                .byId(record.getId())
                .orElseThrow();

        assertThat(found).isEqualTo(new UnusedIngredient(record, sources));

        verifyNoMoreInteractions(store, sources);
    }

    @Test
    void shouldFindUsedById() {
        final var record = savedUsedIngredientRecord();
        when(store.byId(record.getId()))
                .thenReturn(Optional.of(record));

        final var found = ingredients
                .byId(record.getId())
                .orElseThrow();

        assertThat(found).isEqualTo(new UsedIngredient(record, sources));

        verifyNoMoreInteractions(store, sources);
    }

    @Test
    void shouldFindUnusedByCode() {
        final var record = savedUnusedIngredientRecord();
        when(store.byCode(record.getCode()))
                .thenReturn(Optional.of(record));

        final var found = ingredients
                .byCode(record.getCode())
                .orElseThrow();

        assertThat(found).isEqualTo(new UnusedIngredient(record, sources));

        verifyNoMoreInteractions(store, sources);
    }

    @Test
    void shouldFindUsedByCode() {
        final var record = savedUsedIngredientRecord();
        when(store.byCode(record.getCode()))
                .thenReturn(Optional.of(record));

        final var found = ingredients
                .byCode(record.getCode())
                .orElseThrow();

        assertThat(found).isEqualTo(new UsedIngredient(record, sources));

        verifyNoMoreInteractions(store, sources);
    }

    @Test
    void shouldUnusedFindByName() {
        final var record = savedUnusedIngredientRecord();
        when(store.byName(record.getName()))
                .thenReturn(Stream.of(record));

        final var found = ingredients.allByName(record.getName());

        assertThat(found)
                .containsExactly(new UnusedIngredient(record, sources));

        verifyNoMoreInteractions(store, sources);
    }

    @Test
    void shouldUsedFindByName() {
        final var record = savedUsedIngredientRecord();
        when(store.byName(record.getName()))
                .thenReturn(Stream.of(record));

        final var found = ingredients.allByName(record.getName());

        assertThat(found)
                .containsExactly(new UsedIngredient(record, sources));

        verifyNoMoreInteractions(store, sources);
    }

    @Test
    void shouldFindUnused() {
        final var record = savedUnusedIngredientRecord();
        when(store.unused())
                .thenReturn(Stream.of(record));

        final var found = ingredients.allUnused();

        assertThat(found)
                .containsExactly(new UnusedIngredient(record, sources));

        verifyNoMoreInteractions(store, sources);
    }
}
