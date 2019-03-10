package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import hm.binkley.basilisk.flora.domain.store.IngredientStore;
import hm.binkley.basilisk.flora.rest.IngredientRequest;
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
class IngredientsTest {
    @Mock
    private IngredientStore store;

    private Ingredients ingredients;

    @BeforeEach
    void setUp() {
        ingredients = new Ingredients(store);
    }

    @Test
    void shouldFindById() {
        final var record = new IngredientRecord(3L, EPOCH, "EGGS");
        when(store.byId(record.getId()))
                .thenReturn(Optional.of(record));

        final Optional<Ingredient> found = ingredients.byId(record.getId());

        assertThat(found).contains(new Ingredient(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByName() {
        final var record = new IngredientRecord(3L, EPOCH, "MILK");
        when(store.byName(record.getName()))
                .thenReturn(Stream.of(record));

        final Stream<Ingredient> found = ingredients.byName(record.getName());

        assertThat(found).containsExactly(new Ingredient(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindAll() {
        final var record = new IngredientRecord(3L, EPOCH, "SALT");
        when(store.all())
                .thenReturn(Stream.of(record));

        final Stream<Ingredient> found = ingredients.all();

        assertThat(found).containsExactly(new Ingredient(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindUnused() {
        final var record = new IngredientRecord(3L, EPOCH, "SALT");
        when(store.unused())
                .thenReturn(Stream.of(record));

        final Stream<Ingredient> found = ingredients.unused();

        assertThat(found).containsExactly(new Ingredient(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldCreate() {
        final var name = "QUX";
        final var record = new IngredientRecord(3L, EPOCH, name);

        when(store.save(IngredientRecord.raw(record.getName())))
                .thenReturn(record);

        assertThat(ingredients.create(IngredientRequest.builder()
                .name(name)
                .build()))
                .isEqualTo(new Ingredient(record));

        verifyNoMoreInteractions(store);
    }
}
