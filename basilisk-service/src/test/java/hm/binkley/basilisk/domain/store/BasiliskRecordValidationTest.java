package hm.binkley.basilisk.domain.store;

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
                        .at(Instant.ofEpochSecond(1_000_000))
                        .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("word is marked @NonNull");
    }

    @Test
    void shouldComplainWhenWhenIsMissing() {
        assertThatThrownBy(() ->
                BasiliskRecord.builder()
                        .word("IN THE BEGINNING")
                        .at(null)
                        .build())
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("at is marked @NonNull");
    }
}
