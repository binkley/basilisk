package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import hm.binkley.basilisk.flora.domain.store.IngredientStore;
import hm.binkley.basilisk.flora.rest.UnusedIngredientRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static java.math.BigDecimal.ONE;
import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IngredientsTest {
    private final Long CHEF_ID = 17L;

    @Mock
    private IngredientStore store;

    private Ingredients ingredients;

    @BeforeEach
    void setUp() {
        ingredients = new Ingredients(store);
    }

    @Test
    void shouldFindUnusedById() {
        final var record = new IngredientRecord(
                3L, EPOCH, "EGGS", ONE, null, CHEF_ID);
        when(store.byId(record.getId()))
                .thenReturn(Optional.of(record));

        final Optional<Ingredient> found = ingredients.byId(record.getId());

        assertThat(found).contains(new UnusedIngredient(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindUsedById() {
        final var record = new IngredientRecord(
                3L, EPOCH, "EGGS", ONE, 2L, CHEF_ID);
        when(store.byId(record.getId()))
                .thenReturn(Optional.of(record));

        final Optional<Ingredient> found = ingredients.byId(record.getId());

        assertThat(found).contains(new UsedIngredient(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldUnusedFindByName() {
        final var record = new IngredientRecord(
                3L, EPOCH, "MILK", ONE, null, CHEF_ID);
        when(store.byName(record.getName()))
                .thenReturn(Stream.of(record));

        final var found = ingredients.byName(record.getName());

        assertThat(found).containsExactly(new UnusedIngredient(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldUsedFindByName() {
        final var record = new IngredientRecord(
                3L, EPOCH, "MILK", ONE, 2L, CHEF_ID);
        when(store.byName(record.getName()))
                .thenReturn(Stream.of(record));

        final var found = ingredients.byName(record.getName());

        assertThat(found).containsExactly(new UsedIngredient(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindAll() {
        final var unusedRecord = new IngredientRecord(
                3L, EPOCH, "SALT", ONE, null, CHEF_ID);
        final var usedRecord = new IngredientRecord(
                4L, EPOCH, "PEPPER", ONE, 2L, CHEF_ID);
        when(store.all())
                .thenReturn(Stream.of(unusedRecord, usedRecord));

        final Stream<Ingredient> found = ingredients.all();

        assertThat(found).containsExactly(
                new UnusedIngredient(unusedRecord),
                new UsedIngredient(usedRecord));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindUnused() {
        final var record = new IngredientRecord(
                3L, EPOCH, "SALT", ONE, null, CHEF_ID);
        when(store.unused())
                .thenReturn(Stream.of(record));

        final Stream<UnusedIngredient> found = ingredients.unused();

        assertThat(found).containsExactly(new UnusedIngredient(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldCreateUnused() {
        final var name = "QUX";
        final var record = new IngredientRecord(
                3L, EPOCH, name, ONE, null, CHEF_ID);

        when(store.save(IngredientRecord
                .raw(record.getName(), record.getQuantity(),
                        record.getChefId())))
                .thenReturn(record);

        assertThat(ingredients.createUnused(UnusedIngredientRequest.builder()
                .name(name)
                .quantity(ONE)
                .chefId(CHEF_ID)
                .build()))
                .isEqualTo(new UnusedIngredient(record));

        verifyNoMoreInteractions(store);
    }
}
