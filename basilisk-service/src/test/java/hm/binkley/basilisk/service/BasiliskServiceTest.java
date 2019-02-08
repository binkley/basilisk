package hm.binkley.basilisk.service;

import hm.binkley.basilisk.configuration.BasiliskProperties;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BasiliskServiceTest {
    @Test
    void shouldAcceptWords() {
        final BasiliskService service = new BasiliskService(
                BasiliskProperties.builder()
                        .extraWord("Nancy Drew")
                        .build());

        assertThat(service.extra("FU")).isEqualTo("Nancy Drew is FU");
    }
}
