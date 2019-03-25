package hm.binkley.basilisk.flora.source;

import hm.binkley.basilisk.flora.location.Location;
import hm.binkley.basilisk.flora.location.Locations;
import hm.binkley.basilisk.flora.source.store.SourceRecord.LocationRef;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.FloraFixtures.savedLocationRecord;
import static hm.binkley.basilisk.flora.FloraFixtures.savedSourceRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class SourceTest {
    @Mock
    private final Locations locations;

    @Test
    void shouldHaveAvailableAt() {
        final var locationRecord = savedLocationRecord();
        final var location = new Location(locationRecord);
        when(locations.byRef(LocationRef.of(locationRecord)))
                .thenReturn(Optional.of(location));
        final var record = savedSourceRecord()
                .addAvailableAt(locationRecord);
        final var source = new Source(record, locations);

        final var availableAt = source.getAvailableAt();

        assertThat(availableAt).containsExactly(location);
    }

    @Test
    void shouldAddAvailableAt() {
        final var locationRecord = savedLocationRecord();
        final var record = spy(savedSourceRecord());
        final var source = new Source(record, locations);

        source.addAvailableAt(Stream.of(new Location(locationRecord)));

        verify(record).addAvailableAt(locationRecord);
    }

    @Test
    void shouldRemoveAvailableAt() {
        final var locationRecord = savedLocationRecord();
        final var record = spy(savedSourceRecord()
                .addAvailableAt(locationRecord));
        final var source = new Source(record, locations);

        source.removeAvailableAt(Stream.of(new Location(locationRecord)));

        verify(record).removeAvailableAt(locationRecord);
    }
}
