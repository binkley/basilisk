package hm.binkley.basilisk.basilisk.rest;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Stream;

import static java.math.BigDecimal.TEN;
import static java.time.ZoneOffset.UTC;
import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

class BasiliskRequestTest {
    @Test
    void shouldConvert() {
        final var at = OffsetDateTime.of(
                2011, 2, 3, 14, 5, 6, 7_000_000, UTC)
                .toInstant();
        final var request = BasiliskRequest.builder()
                .word("FOO")
                .at(at)
                .cockatrices(List.of(CockatriceRequest.builder()
                        .beakSize(TEN)
                        .build()))
                .build();

        assertThat(request.as(Basilisky::from, Cockatricy::new))
                .isEqualTo(new Basilisky("FOO", at,
                        List.of(new Cockatricy(TEN))));
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    @ToString
    private static final class Basilisky {
        private final String word;
        private final Instant at;
        private final List<Cockatricy> cockatrices;

        private static Basilisky from(final String word, final Instant at,
                final Stream<Cockatricy> cockatrices) {
            return new Basilisky(word, at, cockatrices.collect(toList()));
        }
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    @ToString
    private static final class Cockatricy {
        private final BigDecimal beakSize;

    }
}
