package hm.binkley.basilisk.flora.source.store;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_NAME;
import static hm.binkley.basilisk.flora.FloraFixtures.savedSourceRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedSourceRecord;
import static java.util.stream.Collectors.toSet;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class SourceStoreTest {
    @Mock
    private final SourceRepository springData;

    private SourceStore store;

    @BeforeEach
    void setUp() {
        store = new SourceStore(springData);
    }

    @Test
    void shouldFindById() {
        final var saved = savedSourceRecord();
        when(springData.findById(SOURCE_ID))
                .thenReturn(Optional.of(saved));

        final var found = store.byId(SOURCE_ID).orElseThrow();

        assertThat(found).isEqualTo(saved);
        assertThat(found.store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindByName() {
        final var saved = savedSourceRecord();
        when(springData.findByName(SOURCE_NAME))
                .thenReturn(Optional.of(saved));

        final var found = store.byName(SOURCE_NAME).orElseThrow();

        assertThat(found).isEqualTo(saved);
        assertThat(found.store).isSameAs(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldFindAll() {
        final var saved = savedSourceRecord();
        when(springData.readAll())
                .thenReturn(Stream.of(saved));

        final var found = store.all().collect(toSet());

        assertThat(found).isEqualTo(Set.of(saved));
        assertThat(found.stream().map(it -> it.store)).containsOnly(store);

        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldCreate() {
        final var unsaved = unsavedSourceRecord();
        final var saved = savedSourceRecord();
        when(springData.save(unsaved))
                .thenReturn(saved);

        assertThat(store.create(unsaved.getCode(), unsaved.getName()))
                .isEqualTo(saved);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }

    @Test
    void shouldSave() {
        final var unsaved = unsavedSourceRecord();
        final var saved = savedSourceRecord();

        when(springData.save(unsaved))
                .thenReturn(saved);

        assertThat(store.save(unsaved)).isEqualTo(saved);

        verify(springData).save(unsaved);
        verifyNoMoreInteractions(springData);
    }
}
