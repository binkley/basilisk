package hm.binkley.basilisk.flora.rest;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.SOURCE_CODE;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.SOURCE_NAME;
import static org.assertj.core.api.Assertions.assertThat;

class SourceRequestTest {
    @Test
    void shouldConvert() {
        final var request = SourceRequest.builder()
                .code(SOURCE_CODE)
                .name(SOURCE_NAME)
                .build();

        assertThat(request.as(Sourcey::new))
                .isEqualTo(new Sourcey(request.getCode(), request.getName()));
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    @ToString
    private static final class Sourcey {
        private final String code;
        private final String name;
    }
}
