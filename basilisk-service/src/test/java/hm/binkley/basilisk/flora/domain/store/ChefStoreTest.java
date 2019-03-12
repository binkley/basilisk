package hm.binkley.basilisk.flora.domain.store;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static java.time.Instant.EPOCH;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ChefStoreTest {
    private static final String NAME = "The Dallas Yellow Rose";

    @Mock
    private ChefRepository springData;

    private ChefStore store;

    @BeforeEach
    void setUp() {
        store = new ChefStore(springData);
    }

    @Test
    void shouldFindById() {
        final var id = 3L;
        final var saved = new ChefRecord(id, EPOCH, NAME);
        when(springData.findById(id))
                .thenReturn(Optional.of(saved));

        final var found = store.byId(id).orElseThrow();

        assertThat(found).isEqualTo(saved);
        assertThat(found.store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindByName() {
        final var saved = new ChefRecord(3L, EPOCH, NAME);
        when(springData.findByName(NAME))
                .thenReturn(Optional.of(saved));

        final var found = store.byName(NAME).orElseThrow();

        assertThat(found).isEqualTo(saved);
        assertThat(found.store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindAll() {
        final var saved = new ChefRecord(3L, EPOCH, NAME);
        when(springData.readAll())
                .thenReturn(Stream.of(saved));

        final var found = store.all().collect(toSet());

        assertThat(found).containsOnly(saved);
        assertThat(found.stream().map(it -> it.store)).containsOnly(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldCreate() {
        final var unsaved = ChefRecord.raw(NAME);
        final var saved = new ChefRecord(3L, EPOCH, null);
        when(springData.save(unsaved))
                .thenReturn(saved);

        assertThat(store.create(unsaved.getName()))
                .isEqualTo(saved);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldSave() {
        final var unsaved = ChefRecord.raw(NAME);
        final var saved = new ChefRecord(3L, EPOCH, unsaved.getName());

        when(springData.save(unsaved))
                .thenReturn(saved);

        assertThat(store.save(unsaved)).isEqualTo(saved);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }
}
