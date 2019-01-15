package hm.binkley.basilisk.configuration;

import hm.binkley.basilisk.BasiliskConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = {BasiliskProperties.class, BasiliskConfiguration.class},
        properties = "spring.profiles.active=alternative")
class BasiliskPropertiesTest {
    @Autowired
    private BasiliskProperties basilisk;

    @Test
    void shouldUseAlternativeProfile() {
        assertThat(basilisk.getExtraWord()).isEqualTo("Margaret Hamilton");
    }
}