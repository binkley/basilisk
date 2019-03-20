package hm.binkley.basilisk.flora.rest;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.CHEF_CODE;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.CHEF_NAME;
import static org.assertj.core.api.Assertions.assertThat;

class ChefRequestTest {
    @Test
    void shouldConvert() {
        final var request = ChefRequest.builder()
                .code(CHEF_CODE)
                .name(CHEF_NAME)
                .build();

        assertThat(request.as(Chefey::new))
                .isEqualTo(new Chefey(request.getCode(), request.getName()));
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    @ToString
    private static final class Chefey {
        private final String code;
        private final String name;
    }
}
