package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import hm.binkley.basilisk.flora.domain.store.RecipeRecord;
import hm.binkley.basilisk.flora.domain.store.RecipeStore;
import hm.binkley.basilisk.flora.rest.RecipeRequest;
import hm.binkley.basilisk.flora.rest.UsedIngredientRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.RECIPE_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.unsavedUnusedIngredientRecord;
import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipesTest {
    @Mock
    private RecipeStore store;

    private Recipes recipes;

    @BeforeEach
    void setUp() {
        recipes = new Recipes(store);
    }

    @Test
    void shouldFindById() {
        final var record = new RecipeRecord(
                RECIPE_ID, EPOCH, "SOUFFLE", CHEF_ID);
        when(store.byId(record.getId()))
                .thenReturn(Optional.of(record));

        final Optional<Recipe> found = recipes.byId(record.getId());

        assertThat(found).contains(new Recipe(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByName() {
        final var record = new RecipeRecord(
                RECIPE_ID, EPOCH, "BOILED EGGS", CHEF_ID);
        when(store.byName(record.getName()))
                .thenReturn(Optional.of(record));

        final var found = recipes.byName(record.getName()).orElseThrow();

        assertThat(found).isEqualTo(new Recipe(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindAll() {
        final var record = new RecipeRecord(
                RECIPE_ID, EPOCH, "POACHED EGGS", CHEF_ID);
        when(store.all())
                .thenReturn(Stream.of(record));

        final Stream<Recipe> found = recipes.all();

        assertThat(found).containsExactly(new Recipe(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldCreateNew() {
        final var ingredientRecord = unsavedUnusedIngredientRecord();
        final var record = new RecipeRecord(
                RECIPE_ID, EPOCH, "FRIED EGGS", CHEF_ID)
                .add(ingredientRecord);

        when(store.save(RecipeRecord.raw(
                record.getName(), record.getChefId())
                .add(IngredientRecord.raw(
                        ingredientRecord.getSourceId(),
                        ingredientRecord.getName(),
                        ingredientRecord.getQuantity(),
                        ingredientRecord.getChefId()))))
                .thenReturn(record);

        assertThat(recipes.create(RecipeRequest.builder()
                .name(record.getName())
                .chefId(record.getChefId())
                .ingredients(Set.of(UsedIngredientRequest.builder()
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
