package hm.binkley.basilisk.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BasiliskServiceTest {
    private final BasiliskService service = new BasiliskService();

    @Test
    void shouldAcceptWords() {
        assertThat(service.extra("foo")).isEqualTo("Uncle Bob");
    }
}
