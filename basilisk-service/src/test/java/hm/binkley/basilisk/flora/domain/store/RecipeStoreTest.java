package hm.binkley.basilisk.flora.domain.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static java.time.Instant.EPOCH;
import static java.util.stream.Collectors.toList;
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
        final var saved = new RecipeRecord(3L, EPOCH, "SOUFFLE");
        when(springData.findById(saved.getId()))
                .thenReturn(Optional.of(saved));

        final var found = store.byId(saved.getId());

        assertThat(found).contains(saved);
        assertThat(found.orElseThrow().store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindByName() {
        final var saved = new RecipeRecord(3L, EPOCH, "OMELET");
        when(springData.findByName(saved.getName()))
                .thenReturn(Stream.of(saved));

        final var found = store.byName(saved.getName()).collect(toList());

        assertThat(found).containsExactly(saved);
        assertThat(found.stream().map(it -> it.store)).containsExactly(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindAll() {
        final var saved = new RecipeRecord(3L, EPOCH, "FRIED EGGS");
        when(springData.readAll())
                .thenReturn(Stream.of(saved));

        final var found = store.all().collect(toList());

        assertThat(found).containsExactly(saved);
        assertThat(found.stream().map(it -> it.store)).containsExactly(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldCreate() {
        final var unsaved = RecipeRecord.raw("BOILED EGG");
        final var saved = new RecipeRecord(3L, EPOCH, unsaved
                .getName());
        when(springData.save(unsaved))
                .thenReturn(saved);

        assertThat(store.create(unsaved.getName())).isEqualTo(saved);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldSave() {
        final var unsaved = RecipeRecord.raw("POACHED EGG");
        final var saved = new RecipeRecord(3L, EPOCH,
                unsaved.getName());

        when(springData.save(unsaved))
                .thenReturn(saved);

        assertThat(store.save(unsaved)).isEqualTo(saved);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }
}
