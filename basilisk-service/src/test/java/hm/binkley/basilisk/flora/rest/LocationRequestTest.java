package hm.binkley.basilisk.flora.rest;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.LOCATION_NAME;
import static org.assertj.core.api.Assertions.assertThat;

class LocationRequestTest {
    @Test
    void shouldConvert() {
        final var request = LocationRequest.builder()
                .name(LOCATION_NAME)
                .build();

        assertThat(request.as(Locationy::new))
                .isEqualTo(new Locationy(request.getName()));
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    @ToString
    private static final class Locationy {
        private final String name;
    }
}
