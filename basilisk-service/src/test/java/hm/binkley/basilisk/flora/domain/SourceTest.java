package hm.binkley.basilisk.flora.domain;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.savedSourceRecord;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class SourceTest {
    @Test
    void shouldAs(@Mock final Locations locations) {
        final var record = savedSourceRecord();
        // The types are immaterial, just that the transformation worked
        final var targetSource = 1;

        @SuppressWarnings("PMD") final var that
                = new Source(record, locations).as(
                (id, name) -> {
                    assertThat(id).isEqualTo(record.getId());
                    assertThat(name).isEqualTo(record.getName());
                    return targetSource;
                });

        assertThat(that).isSameAs(targetSource);
    }
}
