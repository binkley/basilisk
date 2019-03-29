package hm.binkley.basilisk.flora.ingredient;

import hm.binkley.basilisk.flora.ingredient.rest.UnusedIngredientRequest;
import hm.binkley.basilisk.flora.ingredient.store.IngredientRecord;
import hm.binkley.basilisk.flora.ingredient.store.IngredientStore;
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
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedUsedIngredientRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class IngredientsTest {
    @Mock
    private final IngredientStore store;

    private Ingredients ingredients;

    @BeforeEach
    void setUp() {
        ingredients = new Ingredients(store);
    }

    @Test
    void shouldCreateUnsaved() {
        final var record = unsavedUsedIngredientRecord();
        when(store.unsaved(
                record.getCode(), record.getSourceId(), record.getName(),
                record.getQuantity(), record.getChefId()))
                .thenReturn(record);

        final var unsaved = ingredients.unsaved(
                record.getCode(), record.getSourceId(), record.getName(),
                record.getQuantity(), record.getChefId());

        assertThat(unsaved).isEqualTo(new UsedIngredient(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindUnusedById() {
        final var record = savedUnusedIngredientRecord();
        when(store.byId(record.getId()))
                .thenReturn(Optional.of(record));

        final var found = ingredients
                .byId(record.getId())
                .orElseThrow();

        assertThat(found).isEqualTo(new UnusedIngredient(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindUsedById() {
        final var record = savedUsedIngredientRecord();
        when(store.byId(record.getId()))
                .thenReturn(Optional.of(record));

        final var found = ingredients
                .byId(record.getId())
                .orElseThrow();

        assertThat(found).isEqualTo(new UsedIngredient(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindUnusedByCode() {
        final var record = savedUnusedIngredientRecord();
        when(store.byCode(record.getCode()))
                .thenReturn(Optional.of(record));

        final var found = ingredients
                .byCode(record.getCode())
                .orElseThrow();

        assertThat(found).isEqualTo(new UnusedIngredient(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindUsedByCode() {
        final var record = savedUsedIngredientRecord();
        when(store.byCode(record.getCode()))
                .thenReturn(Optional.of(record));

        final var found = ingredients
                .byCode(record.getCode())
                .orElseThrow();

        assertThat(found).isEqualTo(new UsedIngredient(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldUnusedFindByName() {
        final var record = savedUnusedIngredientRecord();
        when(store.byName(record.getName()))
                .thenReturn(Stream.of(record));

        final var found = ingredients.allByName(record.getName());

        assertThat(found).containsExactly(new UnusedIngredient(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldUsedFindByName() {
        final var record = savedUsedIngredientRecord();
        when(store.byName(record.getName()))
                .thenReturn(Stream.of(record));

        final var found = ingredients.allByName(record.getName());

        assertThat(found).containsExactly(new UsedIngredient(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindUnused() {
        final var record = savedUnusedIngredientRecord();
        when(store.unused())
                .thenReturn(Stream.of(record));

        final var found = ingredients.allUnused();

        assertThat(found).containsExactly(new UnusedIngredient(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldCreateUnused() {
        final var record = savedUsedIngredientRecord();

        when(store.save(IngredientRecord.unsaved(
                record.getCode(), record.getSourceId(), record.getName(),
                record.getQuantity(), record.getChefId())))
                .thenReturn(record);

        assertThat(ingredients.createUnused(UnusedIngredientRequest.builder()
                .code(record.getCode())
                .sourceId(record.getSourceId())
                .name(record.getName())
                .quantity(record.getQuantity())
                .chefId(record.getChefId())
                .build()))
                .isEqualTo(new UnusedIngredient(record));

        verifyNoMoreInteractions(store);
    }
}
