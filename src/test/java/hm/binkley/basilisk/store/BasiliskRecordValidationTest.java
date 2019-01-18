package hm.binkley.basilisk.store;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

/**
 * These tests show that <em>by construction</em>, the {@code
 * BasiliskRecord} class cannot be constructed with missing data (thanks to
 * Lombok).
 */
class BasiliskRecordValidationTest {
    @Test
    void shouldComplainWhenWordIsMissing() {
        assertThatThrownBy(() ->
                BasiliskRecord.builder()
                        .word(null)
                        .when(Instant.ofEpochSecond(1_000_000))
                        .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("word is marked @NonNull");
    }

    @Test
    void shouldComplainWhenWhenIsMissing() {
        assertThatThrownBy(() ->
                BasiliskRecord.builder()
                        .word("IN THE BEGINNING")
                        .when(null)
                        .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("when is marked @NonNull");
    }
}
