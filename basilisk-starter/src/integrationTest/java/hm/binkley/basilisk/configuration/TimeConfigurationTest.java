package hm.binkley.basilisk.configuration;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Clock;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TimeConfiguration.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class TimeConfigurationTest {
    private final Clock clock;

    @Test
    void shouldMeasureTimeInUtc() {
        assertThat(clock.getZone()).isEqualTo(UTC);
    }
}
