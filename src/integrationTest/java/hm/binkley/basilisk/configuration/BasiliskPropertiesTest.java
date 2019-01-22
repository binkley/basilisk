package hm.binkley.basilisk.configuration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(
        classes = {BasiliskConfiguration.class},
        properties = "spring.profiles.active=test-basilisk-properties")
class BasiliskPropertiesTest {
    @Autowired
    private BasiliskProperties basilisk;

    @Test
    void shouldUseAlternativeProfile() {
        assertThat(basilisk.getExtraWord()).isEqualTo("Margaret Hamilton");
    }
}
