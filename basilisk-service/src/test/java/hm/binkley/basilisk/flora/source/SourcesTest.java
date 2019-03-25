package hm.binkley.basilisk.flora.source;

import hm.binkley.basilisk.flora.location.Locations;
import hm.binkley.basilisk.flora.source.store.SourceStore;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static hm.binkley.basilisk.flora.FloraFixtures.savedSourceRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedSourceRecord;
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
    void shouldCreateUnsaved() {
        final var record = unsavedSourceRecord();
        when(store.unsaved(record.getCode(), record.getName()))
                .thenReturn(record);

        final var unsaved = sources
                .unsaved(record.getCode(), record.getName());

        assertThat(unsaved).isEqualTo(new Source(record, locations));

        verifyNoMoreInteractions(store, locations);
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
}
