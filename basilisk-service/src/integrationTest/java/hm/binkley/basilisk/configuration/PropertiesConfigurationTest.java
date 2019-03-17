package hm.binkley.basilisk.configuration;

import hm.binkley.basilisk.flora.configuration.FloraProperties;
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
        "basilisk.overlapped.endpoint-base=ftp://old/school",
        "flora.nested.number=7",
        "flora.overlapped.endpoint-base=ftp://old/school"
})
class PropertiesConfigurationTest {
    private final BasiliskProperties basilisk;
    private final FloraProperties flora;

    @Test
    void shouldUseAlternativeProfile() {
        assertThat(basilisk.getExtraWord()).isEqualTo("Margaret Hamilton");
        assertThat(flora.getDailySpecial()).isEqualTo("POTATO");
    }

    @Test
    void shouldFindNestedProperties() {
        assertThat(basilisk.getNested().getNumber()).isEqualTo(7);
        assertThat(flora.getNested().getNumber()).isEqualTo(7);
    }

    @Test
    void shouldFindOverlappedProperties() {
        assertThat(basilisk.getOverlapped().getEndpointBase())
                .isEqualTo(URI.create("ftp://old/school"));
        assertThat(flora.getOverlapped().getEndpointBase())
                .isEqualTo(URI.create("ftp://old/school"));
    }
}
