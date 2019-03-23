package hm.binkley.basilisk.flora.source;

import hm.binkley.basilisk.flora.location.Locations;
import hm.binkley.basilisk.flora.source.rest.SourceRequest;
import hm.binkley.basilisk.flora.source.store.SourceStore;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.FloraFixtures.savedSourceRecord;
import static hm.binkley.basilisk.flora.source.store.SourceRecord.unsaved;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class SourcesTest {
    @Mock
    private final SourceStore store;
    @Mock
    private final Locations locations;

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

        final var found = sources
                .byId(saved.getId())
                .orElseThrow();

        assertThat(found).isEqualTo(new Source(saved, locations));

        verifyNoMoreInteractions(store, locations);
    }

    @Test
    void shouldFindByCode() {
        final var record = savedSourceRecord();
        when(store.byCode(record.getCode()))
                .thenReturn(Optional.of(record));

        final var found = sources
                .byCode(record.getCode())
                .orElseThrow();

        assertThat(found).isEqualTo(new Source(record, locations));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByName() {
        final var saved = savedSourceRecord();
        when(store.byName(saved.getName()))
                .thenReturn(Optional.of(saved));

        final var found = sources
                .byName(saved.getName())
                .orElseThrow();

        assertThat(found).isEqualTo(new Source(saved, locations));

        verifyNoMoreInteractions(store, locations);
    }

    @Test
    void shouldFindAll() {
        final var saved = savedSourceRecord();
        when(store.all())
                .thenReturn(Stream.of(saved));

        final var found = sources.all();

        assertThat(found).containsExactly(new Source(saved, locations));

        verifyNoMoreInteractions(store, locations);
    }

    @Test
    void shouldCreateNew() {
        final var saved = savedSourceRecord();
        when(store.save(unsaved(saved.getCode(), saved.getName())))
                .thenReturn(saved);

        assertThat(sources.create(SourceRequest.builder()
                .code(saved.getCode())
                .name(saved.getName())
                .build()))
                .isEqualTo(new Source(saved, locations));

        verifyNoMoreInteractions(store, locations);
    }
}
