package hm.binkley.basilisk.domain.store;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * These tests show that <em>by construction</em>, the {@code BasiliskRecord}
 * class cannot be constructed with missing data (thanks to Lombok).
 */
class BasiliskRecordValidationTest {
    @Test
    void shouldComplainWhenWordIsMissing() {
        assertThatThrownBy(() ->
                new BasiliskRecord(null, null, null,
                        Instant.ofEpochSecond(1_000_000)))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("word is marked @NonNull");
    }

    @Test
    void shouldComplainWhenWhenIsMissing() {
        assertThatThrownBy(() ->
                new BasiliskRecord(null, null, "IN THE BEGINNING", null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("at is marked @NonNull");
    }
}
