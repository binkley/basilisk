package hm.binkley.basilisk.flora.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.savedLocationRecord;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.savedSourceRecord;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SourceTest {
    @Test
    void shouldAs(@Mock final Locations locations) {
        final var locationRecord = savedLocationRecord();
        when(locations.byRef(savedLocationRecord().ref()))
                .thenReturn(Optional.of(new Location(locationRecord)));
        final var record = savedSourceRecord()
                .addAvailableAt(locationRecord);
        // The types are immaterial, just that the transformation worked
        final var targetSource = 1;
        final var targetLocation = 2;

        @SuppressWarnings("PMD") final var that
                = new Source(record, locations).as(
                (id, code, name, availableAt) -> {
                    assertThat(code).isEqualTo(record.getCode());
                    assertThat(name).isEqualTo(record.getName());
                    assertThat(availableAt).containsExactly(targetLocation);
                    return targetSource;
                }, (lid, lcode, lname) -> {
                    assertThat(lid).isEqualTo(locationRecord.getId());
                    assertThat(lcode).isEqualTo(locationRecord.getCode());
                    assertThat(lname).isEqualTo(locationRecord.getName());
                    return targetLocation;
                });

        assertThat(that).isSameAs(targetSource);
    }
}
