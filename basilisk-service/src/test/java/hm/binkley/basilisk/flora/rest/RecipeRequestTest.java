package hm.binkley.basilisk.flora.rest;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeRequestTest {
    @Test
    void shouldConvert() {
        final var request = RecipeRequest.builder()
                .name("SOUFFLE")
                .build();

        assertThat(request.as(Recipey::new))
                .isEqualTo(new Recipey(request.getName()));
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    @ToString
    private static final class Recipey {
        private final String name;
    }
}
