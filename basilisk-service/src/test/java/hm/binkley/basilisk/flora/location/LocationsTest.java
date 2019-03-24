package hm.binkley.basilisk.flora.location;

import hm.binkley.basilisk.flora.location.store.LocationStore;
import hm.binkley.basilisk.flora.source.store.SourceRecord.LocationRef;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static hm.binkley.basilisk.flora.FloraFixtures.savedLocationRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedLocationRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class LocationsTest {
    @Mock
    private final LocationStore store;

    private Locations locations;

    @BeforeEach
    void setUp() {
        locations = new Locations(store);
    }

    @Test
    void shouldCreateUnsaved() {
        final var record = unsavedLocationRecord();
        when(store.unsaved(record.getCode(), record.getName()))
                .thenReturn(record);

        final var unsaved = locations.unsaved(
                record.getCode(), record.getName());

        assertThat(unsaved).isEqualTo(new Location(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByRef() {
        final var record = savedLocationRecord();
        when(store.byId(record.getId()))
                .thenReturn(Optional.of(record));

        final var found = locations.byRef(LocationRef.of(record))
                .orElseThrow();

        assertThat(found).isEqualTo(new Location(record));

        verifyNoMoreInteractions(store);
    }

    @Test
    void shouldFindByName() {
        final var record = savedLocationRecord();
        when(store.byName(record.getName()))
                .thenReturn(Optional.of(record));

        final var found = locations.byName(record.getName()).orElseThrow();

        assertThat(found).isEqualTo(new Location(record));

        verifyNoMoreInteractions(store);
    }
}
