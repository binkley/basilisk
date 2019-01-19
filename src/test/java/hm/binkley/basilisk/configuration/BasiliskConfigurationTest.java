package hm.binkley.basilisk.configuration;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class BasiliskConfigurationTest {
    @Test
    void shouldWorkWithMissingContact() {
        final var apiInfo = new BasiliskConfiguration()
                .apiInfo(SwaggerProperties.builder()
                        .contact(null)
                        .build());

        assertThat(apiInfo.getContact()).isNotNull();
    }

    @Test
    void shouldWorkWithMissingContactDetails() {
        final var apiInfo = new BasiliskConfiguration()
                .apiInfo(SwaggerProperties.builder()
                        .contact(SwaggerProperties.Contact.builder()
                                .build())
                        .build());

        assertThat(apiInfo.getContact()).isNotNull();
    }
}
