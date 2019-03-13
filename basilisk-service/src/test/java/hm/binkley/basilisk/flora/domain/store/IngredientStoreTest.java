package hm.binkley.basilisk.flora.domain.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static java.time.Instant.EPOCH;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IngredientStoreTest {
    private static final Long CHEF_ID = 17L;

    @Mock
    private IngredientRepository springData;

    private IngredientStore store;

    @BeforeEach
    void setUp() {
        store = new IngredientStore(springData);
    }

    @Test
    void shouldFindById() {
        final var id = 3L;
        final var saved = new IngredientRecord(
                id, EPOCH, "EGGS", null, CHEF_ID);
        when(springData.findById(id))
                .thenReturn(Optional.of(saved));

        final var found = store.byId(id).orElseThrow();

        assertThat(found).isEqualTo(saved);
        assertThat(found.store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindByName() {
        final var name = "BACON";
        final var saved = new IngredientRecord(
                3L, EPOCH, name, null, CHEF_ID);
        when(springData.findByName(name))
                .thenReturn(Optional.of(saved));

        final var found = store.byName(name).orElseThrow();

        assertThat(found).isEqualTo(saved);
        assertThat(found.store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindUnused() {
        final var saved = new IngredientRecord(
                3L, EPOCH, "THYME", null, CHEF_ID);
        when(springData.findAllByRecipeIdIsNull()).
                thenReturn(Stream.of(saved));

        final var found = store.unused().collect(toSet());

        assertThat(found).isEqualTo(Set.of(saved));
        assertThat(found.stream().map(it -> it.store)).containsOnly(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindAll() {
        final var saved = new IngredientRecord(
                3L, EPOCH, "MILK", null, CHEF_ID);
        when(springData.readAll())
                .thenReturn(Stream.of(saved));

        final var found = store.all().collect(toSet());

        assertThat(found).isEqualTo(Set.of(saved));
        assertThat(found.stream().map(it -> it.store)).containsOnly(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldCreate() {
        final var unsaved = IngredientRecord.raw("SALT", CHEF_ID);
        final var saved = new IngredientRecord(
                3L, EPOCH, unsaved.getName(), null, CHEF_ID);
        when(springData.save(unsaved))
                .thenReturn(saved);

        assertThat(store.create(
                unsaved.getName(), unsaved.getChefId()))
                .isEqualTo(saved);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldSave() {
        final var unsaved = IngredientRecord.raw("PEPPER", CHEF_ID);
        final var saved = new IngredientRecord(
                3L, EPOCH, unsaved.getName(), 2L, CHEF_ID);

        when(springData.save(unsaved))
                .thenReturn(saved);

        assertThat(store.save(unsaved)).isEqualTo(saved);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }
}
