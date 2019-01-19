package hm.binkley.basilisk.configuration;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BasiliskConfigurationTest {
    @Test
    void shouldWorkWithMissingContactDetails() {
        final var apiInfo = new BasiliskConfiguration()
                .apiInfo(new SwaggerProperties());

        assertThat(apiInfo.getContact()).isNotNull();
    }
}
