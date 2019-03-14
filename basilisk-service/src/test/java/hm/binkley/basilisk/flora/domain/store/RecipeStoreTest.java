package hm.binkley.basilisk.flora.domain.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.CHEF_ID;
import static java.time.Instant.EPOCH;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeStoreTest {
    @Mock
    private RecipeRepository springData;

    private RecipeStore store;

    @BeforeEach
    void setUp() {
        store = new RecipeStore(springData);
    }

    @Test
    void shouldFindById() {
        final var saved = new RecipeRecord(
                3L, EPOCH, "SOUFFLE", CHEF_ID);
        when(springData.findById(saved.getId()))
                .thenReturn(Optional.of(saved));

        final var found = store.byId(saved.getId()).orElseThrow();

        assertThat(found).isEqualTo(saved);
        assertThat(found.store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindByName() {
        final var saved = new RecipeRecord(
                3L, EPOCH, "OMELET", CHEF_ID);
        when(springData.findByName(saved.getName()))
                .thenReturn(Optional.of(saved));

        final var found = store.byName(saved.getName()).orElseThrow();

        assertThat(found).isEqualTo(saved);
        assertThat(found.store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindAll() {
        final var saved = new RecipeRecord(
                3L, EPOCH, "FRIED EGGS", CHEF_ID);
        when(springData.readAll())
                .thenReturn(Stream.of(saved));

        final var found = store.all().collect(toSet());

        assertThat(found).isEqualTo(Set.of(saved));
        assertThat(found.stream().map(it -> it.store)).containsOnly(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldCreate() {
        final var unsaved = RecipeRecord.raw("BOILED EGG", CHEF_ID);
        final var id = 3L;
        final var saved = new RecipeRecord(
                id, EPOCH, unsaved.getName(), CHEF_ID);
        when(springData.save(unsaved))
                .thenReturn(saved);
        when(springData.findById(id)) // TODO: See RecipeRepositoryTest
                .thenReturn(Optional.of(saved));

        assertThat(store.create(unsaved.getName(), unsaved.getChefId()))
                .isEqualTo(saved);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldSave() {
        final var unsaved = RecipeRecord.raw("POACHED EGG", CHEF_ID);
        final var id = 3L;
        final var saved = new RecipeRecord(
                id, EPOCH, unsaved.getName(), CHEF_ID);

        when(springData.save(unsaved))
                .thenReturn(saved);
        when(springData.findById(id)) // TODO: See RecipeRepositoryTest
                .thenReturn(Optional.of(saved));

        assertThat(store.save(unsaved)).isEqualTo(saved);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }
}
