package hm.binkley.basilisk.flora.rest;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ChefRequestTest {
    @Test
    void shouldConvert() {
        final var request = ChefRequest.builder()
                .name("Chef Bob")
                .build();

        assertThat(request.as(Chefey::new))
                .isEqualTo(new Chefey(request.getName()));
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    @ToString
    private static final class Chefey {
        private final String name;
    }
}
