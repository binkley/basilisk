package hm.binkley.basilisk.flora.source.store;

import hm.binkley.basilisk.flora.location.store.LocationRecord;
import hm.binkley.basilisk.flora.location.store.LocationStore;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static hm.binkley.basilisk.flora.FloraFixtures.savedLocationRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.savedSourceRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedLocationRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedSourceRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class SourceRecordTest {
    @Mock
    private final SourceStore store;
    @Mock
    private final LocationStore locationStore;

    @Test
    void shouldSave() {
        final var unsavedLocation = unsavedLocationRecord();
        unsavedLocation.store = locationStore;
        final var savedLocation = savedLocationRecord();
        savedLocation.store = locationStore;
        when(locationStore.save(unsavedLocation))
                .thenReturn(savedLocation);
        final var unsaved = unsavedSourceRecord()
                .addAvailableAt(unsavedLocation);
        unsaved.store = store;
        final var saved = savedSourceRecord()
                .addAvailableAt(savedLocation);
        saved.store = store;
        when(store.save(unsaved))
                .thenReturn(saved);

        assertThat(unsaved.save()).isEqualTo(saved);

        verify(locationStore).save(unsavedLocation);
        verify(store).save(unsaved);
        verifyNoMoreInteractions(store, locationStore);
    }

    @Test
    void shouldComplainIfLocationMissing() {
        assertThatThrownBy(() ->
                unsavedSourceRecord().addAvailableAt((LocationRecord) null))
                .isInstanceOf(NullPointerException.class);
    }
}
