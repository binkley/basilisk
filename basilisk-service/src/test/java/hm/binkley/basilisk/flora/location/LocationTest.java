package hm.binkley.basilisk.flora.location;

import org.junit.jupiter.api.Test;

import static hm.binkley.basilisk.flora.FloraFixtures.savedLocationRecord;
import static org.assertj.core.api.Assertions.assertThat;

class LocationTest {
    @Test
    void shouldAs() {
        final var record = savedLocationRecord();
        // The types are immaterial, just that the transformation worked
        final var targetLocation = 1;

        @SuppressWarnings("PMD") final var that
                = new Location(record).as(
                (id, code, name) -> {
                    assertThat(id).isEqualTo(record.getId());
                    assertThat(code).isEqualTo(record.getCode());
                    assertThat(name).isEqualTo(record.getName());
                    return targetLocation;
                });

        assertThat(that).isSameAs(targetLocation);
    }
}
