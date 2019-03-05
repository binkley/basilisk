package hm.binkley.basilisk.rest;

import hm.binkley.basilisk.rest.HappyTest.FakeTimeConfiguration;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;

import static java.time.Instant.EPOCH;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = {Happy.class, FakeTimeConfiguration.class})
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class HappyTest {
    private final Happy happy;
    private final Clock clock;

    @Test
    void shouldBeUpAtEpoch() {
        assertThat(happy.health().getDetails()).isEqualTo(Map.of(
                "apple", "core",
                "friend?", Instant.now(clock)));
    }

    @TestConfiguration
    public static class FakeTimeConfiguration {
        @Bean
        public Clock epochClock() {
            return Clock.fixed(EPOCH, UTC);
        }
    }
}
