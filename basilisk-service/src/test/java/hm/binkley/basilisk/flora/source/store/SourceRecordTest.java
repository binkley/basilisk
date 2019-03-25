package hm.binkley.basilisk.flora.source.store;

import hm.binkley.basilisk.flora.location.store.LocationRecord;
import hm.binkley.basilisk.flora.source.store.SourceRecord.LocationRef;
import org.junit.jupiter.api.Test;

import java.util.NoSuchElementException;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.FloraFixtures.LOCATION_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.LOCATION_RECEIVED_AT;
import static hm.binkley.basilisk.flora.FloraFixtures.savedLocationRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.savedSourceRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedLocationRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.unsavedSourceRecord;
import static hm.binkley.basilisk.store.PersistenceTesting.simulateRecordSave;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

class SourceRecordTest {
    @Test
    void shouldAddAvailableAt() {
        final var locationRecord = savedLocationRecord();
        final var record = savedSourceRecord();

        record.addAvailableAt(Stream.of(locationRecord));

        assertThat(record.getAvailableAt())
                .containsExactly(LocationRef.of(locationRecord));
    }

    @Test
    void shouldRemoveAvailableAt() {
        final var locationRecord = savedLocationRecord();
        final var record = savedSourceRecord()
                .addAvailableAt(locationRecord);

        record.removeAvailableAt(Stream.of(locationRecord));

        assertThat(record.getAvailableAt()).isEmpty();
    }

    @Test
    void shouldCreateLocationRefWhenLocationIsSaved() {
        final var locationRecord = spy(savedLocationRecord());

        final var ref = LocationRef.of(locationRecord);

        assertThat(ref.getLocationId()).isEqualTo(locationRecord.getId());

        verify(locationRecord, never()).save();
    }

    @Test
    void shouldCreateLocationRefWhenLocationIsUnsaved() {
        final var locationRecord = spy(unsavedLocationRecord());
        doAnswer(simulateRecordSave(LOCATION_ID, LOCATION_RECEIVED_AT))
                .when(locationRecord).save();

        final var ref = LocationRef.of(locationRecord);

        assertThat(locationRecord.getId()).isNotNull();
        assertThat(ref.getLocationId()).isEqualTo(locationRecord.getId());

        verify(locationRecord).save();
    }

    @Test
    void shouldComplainOnMissingLocation() {
        assertThatThrownBy(() ->
                unsavedSourceRecord().addAvailableAt((LocationRecord) null))
                .isInstanceOf(NullPointerException.class);
    }

    @Test
    void shouldComplainOnDuplicateLocation() {
        assertThatThrownBy(() -> {
            final var locationRecord = savedLocationRecord();

            savedSourceRecord()
                    .addAvailableAt(locationRecord)
                    .addAvailableAt(locationRecord);
        }).isInstanceOf(IllegalStateException.class);
    }

    @Test
    void shouldComplainOnAbsentLocation() {
        assertThatThrownBy(() -> savedSourceRecord().removeAvailableAt(
                savedLocationRecord()))
                .isInstanceOf(NoSuchElementException.class);
    }
}
