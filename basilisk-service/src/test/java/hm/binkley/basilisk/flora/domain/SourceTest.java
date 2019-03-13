package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.SourceRecord;
import org.junit.jupiter.api.Test;

import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;

class SourceTest {
    @Test
    void shouldAs() {
        final var record = new SourceRecord(
                3L, EPOCH, "EGGS");
        // The types are immaterial, just that the transformation worked
        final var targetSource = 1;

        @SuppressWarnings("PMD") final var that
                = new Source(record).as(
                (id, name) -> {
                    assertThat(id).isEqualTo(record.getId());
                    assertThat(name).isEqualTo(record.getName());
                    return targetSource;
                });

        assertThat(that).isSameAs(targetSource);
    }
}
