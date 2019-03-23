package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import hm.binkley.basilisk.flora.domain.store.RecipeRecord;
import hm.binkley.basilisk.flora.domain.store.RecipeStore;
import hm.binkley.basilisk.flora.rest.RecipeRequest;
import hm.binkley.basilisk.flora.rest.UsedIngredientRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.savedRecipeRecord;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.unsavedUnusedIngredientRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class RecipesTest {
    @Mock
    private final RecipeStore store;

    private Recipes recipes;

    @BeforeEach
    void setUp() {
        recipes = new Recipes(store);
    }

    @Test
    void shouldFindById() {
        final var record = savedRecipeRecord();
        when(store.byId(record.getId()))
                .thenReturn(Optional.of(record));

        final Optional<Recipe> found = recipes.byId(record.getId());

        assertThat(found).contains(new Recipe(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByCode() {
        final var record = savedRecipeRecord();
        when(store.byCode(record.getCode()))
                .thenReturn(Optional.of(record));

        final var found = recipes.byCode(record.getCode()).orElseThrow();

        assertThat(found).isEqualTo(new Recipe(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByName() {
        final var record = savedRecipeRecord();
        when(store.byName(record.getName()))
                .thenReturn(Optional.of(record));

        final var found = recipes.byName(record.getName()).orElseThrow();

        assertThat(found).isEqualTo(new Recipe(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindAll() {
        final var record = savedRecipeRecord();
        when(store.all())
                .thenReturn(Stream.of(record));

        final Stream<Recipe> found = recipes.all();

        assertThat(found).containsExactly(new Recipe(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldCreateNew() {
        final var ingredientRecord = unsavedUnusedIngredientRecord();
        final var record = savedRecipeRecord()
                .add(ingredientRecord);

        when(store.save(RecipeRecord.unsaved(
                record.getCode(), record.getName(), record.getChefId())
                .add(IngredientRecord.unsaved(
                        ingredientRecord.getCode(),
                        ingredientRecord.getSourceId(),
                        ingredientRecord.getName(),
                        ingredientRecord.getQuantity(),
                        ingredientRecord.getChefId()))))
                .thenReturn(record);

        assertThat(recipes.create(RecipeRequest.builder()
                .code(record.getCode())
                .name(record.getName())
                .chefId(record.getChefId())
                .ingredients(Set.of(UsedIngredientRequest.builder()
                        .code(ingredientRecord.getCode())
                        .sourceId(ingredientRecord.getSourceId())
                        .name(ingredientRecord.getName())
                        .quantity(ingredientRecord.getQuantity())
                        .chefId(ingredientRecord.getChefId())
                        .build()))
                .build()))
                .isEqualTo(new Recipe(record));

        verifyNoMoreInteractions(store);
    }
}
