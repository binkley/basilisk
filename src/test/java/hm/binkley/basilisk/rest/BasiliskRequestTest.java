package hm.binkley.basilisk.rest;

import hm.binkley.basilisk.store.BasiliskRecord;
import org.junit.jupiter.api.Test;

import java.time.OffsetDateTime;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;

class BasiliskRequestTest {
    @Test
    void shouldConvertToRecord() {
        final var when = OffsetDateTime.of(
                2011, 2, 3, 4, 5, 6, 7_000_000, UTC)
                .toInstant();
        final var request = BasiliskRequest.builder()
                .word("FOO")
                .when(when)
                .build();

        assertThat(request.toRecord()).isEqualTo(
                BasiliskRecord.builder()
                        .word(request.getWord())
                        .when(when)
                        .build());
    }
}
