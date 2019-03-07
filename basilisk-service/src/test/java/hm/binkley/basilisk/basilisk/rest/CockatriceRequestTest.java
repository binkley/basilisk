package hm.binkley.basilisk.basilisk.rest;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static java.math.BigDecimal.TEN;
import static org.assertj.core.api.Assertions.assertThat;

class CockatriceRequestTest {
    @Test
    void shouldConvert() {
        final var request = CockatriceRequest.builder()
                .beakSize(TEN)
                .build();

        assertThat(request.as(Cockatricy::new))
                .isEqualTo(new Cockatricy(TEN));
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    @ToString
    private static final class Cockatricy {
        private final BigDecimal beakSize;

    }
}
