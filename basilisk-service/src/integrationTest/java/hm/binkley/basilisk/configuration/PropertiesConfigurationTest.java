package hm.binkley.basilisk.configuration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.URI;

import static org.assertj.core.api.Assertions.assertThat;

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SpringBootTest(classes = PropertiesConfiguration.class, properties = {
        "spring.profiles.active=test",
        "basilisk.nested.number=7",
        "basilisk.overlapped.endpoint-base=ftp://old/school"
})
class PropertiesConfigurationTest {
    private final BasiliskProperties basilisk;

    @Test
    void shouldUseAlternativeProfile() {
        assertThat(basilisk.getExtraWord()).isEqualTo("Margaret Hamilton");
    }

    @Test
    void shouldFindNestedProperties() {
        assertThat(basilisk.getNested().getNumber()).isEqualTo(7);
    }

    @Test
    void shouldFindOverlappedProperties() {
        assertThat(basilisk.getOverlapped().getEndpointBase())
                .isEqualTo(URI.create("ftp://old/school"));
    }
}