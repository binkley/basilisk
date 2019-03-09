package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.RecipeRecord;
import hm.binkley.basilisk.flora.domain.store.RecipeStore;
import hm.binkley.basilisk.flora.rest.RecipeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipesTest {
    @Mock
    private RecipeStore store;

    private Recipes ingredients;

    @BeforeEach
    void setUp() {
        ingredients = new Recipes(store);
    }

    @Test
    void shouldFindById() {
        final var record = new RecipeRecord(3L, EPOCH, "SOUFFLE");
        when(store.byId(record.getId()))
                .thenReturn(Optional.of(record));

        final Optional<Recipe> found = ingredients.byId(record.getId());

        assertThat(found).contains(new Recipe(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByName() {
        final var record = new RecipeRecord(3L, EPOCH, "BOILED EGGS");
        when(store.byName(record.getName()))
                .thenReturn(Stream.of(record));

        final Stream<Recipe> found = ingredients.byName(record.getName());

        assertThat(found).containsExactly(new Recipe(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindAll() {
        final var record = new RecipeRecord(3L, EPOCH, "POACHED EGGS");
        when(store.all())
                .thenReturn(Stream.of(record));

        final Stream<Recipe> found = ingredients.all();

        assertThat(found).containsExactly(new Recipe(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldCreate() {
        final var name = "FRIED EGGS";
        final var record = new RecipeRecord(3L, EPOCH, name);

        when(store.save(RecipeRecord.raw(record.getName())))
                .thenReturn(record);

        assertThat(ingredients.create(RecipeRequest.builder()
                .name(name)
                .build()))
                .isEqualTo(new Recipe(record));

        verifyNoMoreInteractions(store);
    }
}
