package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.SourceStore;
import hm.binkley.basilisk.flora.rest.SourceRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.savedLocationRecord;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.savedSourceRecord;
import static hm.binkley.basilisk.flora.domain.store.SourceRecord.unsaved;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SourcesTest {
    @Mock
    private SourceStore store;
    @Mock
    private Locations locations;

    private Sources sources;

    @BeforeEach
    void setUp() {
        sources = new Sources(store, locations);
    }

    @Test
    void shouldFindById() {
        final var saved = savedSourceRecord();
        when(store.byId(saved.getId()))
                .thenReturn(Optional.of(saved));

        final Optional<Source> found = sources.byId(saved.getId());

        assertThat(found).contains(new Source(saved, Set.of()));

        verifyNoMoreInteractions(store, locations);
    }

    @Test
    void shouldFindByIdWithAvailableAt() {
        final var locationRecord = savedLocationRecord();
        final var saved = savedSourceRecord()
                .availableAt(locationRecord);
        when(store.byId(saved.getId()))
                .thenReturn(Optional.of(saved));
        final var location = new Location(locationRecord);
        when(locations.byRef(locationRecord.ref()))
                .thenReturn(Optional.of(location));

        final Optional<Source> found = sources.byId(saved.getId());

        assertThat(found).contains(new Source(saved, Set.of(location)));

        verifyNoMoreInteractions(store, locations);
    }

    @Test
    void shouldFindByName() {
        final var saved = savedSourceRecord();
        when(store.byName(saved.getName()))
                .thenReturn(Optional.of(saved));

        final var found = sources.byName(saved.getName()).orElseThrow();

        assertThat(found).isEqualTo(new Source(saved, Set.of()));

        verifyNoMoreInteractions(store, locations);
    }

    @Test
    void shouldFindAll() {
        final var saved = savedSourceRecord();
        when(store.all())
                .thenReturn(Stream.of(saved));

        final Stream<Source> found = sources.all();

        assertThat(found).containsExactly(new Source(saved, Set.of()));

        verifyNoMoreInteractions(store, locations);
    }

    @Test
    void shouldCreateNew() {
        final var saved = savedSourceRecord();
        when(store.save(unsaved(saved.getName())))
                .thenReturn(saved);

        assertThat(sources.create(SourceRequest.builder()
                .name(saved.getName())
                .build()))
                .isEqualTo(new Source(saved, Set.of()));

        verifyNoMoreInteractions(store, locations);
    }
}
