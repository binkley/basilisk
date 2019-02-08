package hm.binkley.basilisk.configuration;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SwaggerConfigurationTest {
    @Test
    void shouldWorkWithMissingContactDetails() {
        final var apiInfo = new SwaggerConfiguration()
                .apiInfo(new SwaggerProperties());

        assertThat(apiInfo.getContact()).isNotNull();
    }
}
