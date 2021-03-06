package hm.binkley.basilisk.flora.recipe;

import hm.binkley.basilisk.flora.ingredient.rest.UsedIngredientRequest;
import hm.binkley.basilisk.flora.ingredient.store.IngredientRecord;
import hm.binkley.basilisk.flora.recipe.rest.RecipeRequest;
import hm.binkley.basilisk.flora.recipe.store.RecipeRecord;
import hm.binkley.basilisk.flora.recipe.store.RecipeStore;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.FloraFixtures.savedRecipeRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedUnusedIngredientRecord;
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

        final var found = recipes
                .byId(record.getId())
                .orElseThrow();

        assertThat(found).isEqualTo(new Recipe(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByCode() {
        final var record = savedRecipeRecord();
        when(store.byCode(record.getCode()))
                .thenReturn(Optional.of(record));

        final var found = recipes
                .byCode(record.getCode())
                .orElseThrow();

        assertThat(found).isEqualTo(new Recipe(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByName() {
        final var record = savedRecipeRecord();
        when(store.byName(record.getName()))
                .thenReturn(Optional.of(record));

        final var found = recipes
                .byName(record.getName())
                .orElseThrow();

        assertThat(found).isEqualTo(new Recipe(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindAll() {
        final var record = savedRecipeRecord();
        when(store.all())
                .thenReturn(Stream.of(record));

        final var found = recipes.all();

        assertThat(found).containsExactly(new Recipe(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldCreateNew() {
        final var ingredientRecord = unsavedUnusedIngredientRecord();
        final var record = savedRecipeRecord()
                .addUnusedIngredient(ingredientRecord);

        when(store.save(RecipeRecord.unsaved(
                record.getCode(), record.getName(), record.getChefId())
                .addUnusedIngredient(IngredientRecord.unsaved(
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
                .ingredients(new TreeSet<>(Set.of(
                        UsedIngredientRequest.builder()
                                .code(ingredientRecord.getCode())
                                .sourceId(ingredientRecord.getSourceId())
                                .name(ingredientRecord.getName())
                                .quantity(ingredientRecord.getQuantity())
                                .chefId(ingredientRecord.getChefId())
                                .build())))
                .build()))
                .isEqualTo(new Recipe(record));

        verifyNoMoreInteractions(store);
    }
}
