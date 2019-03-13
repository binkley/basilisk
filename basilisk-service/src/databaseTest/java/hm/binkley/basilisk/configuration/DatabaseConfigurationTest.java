package hm.binkley.basilisk.configuration;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.auditing.DateTimeProvider;

import java.time.Clock;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static java.time.Instant.EPOCH;
import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;

@AutoConfigureEmbeddedDatabase
@DataJdbcTest
@Import(DatabaseConfiguration.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class DatabaseConfigurationTest {
    private final DateTimeProvider provider;

    /** @todo Better test without cast */
    @Test
    void shouldProvideUtcClock() {
        final var nowish = (ZonedDateTime) provider.getNow().orElseThrow();

        assertThat(nowish).isEqualTo(ZonedDateTime.ofInstant(EPOCH, UTC));
    }

    @TestConfiguration
    public static class MyClock {
        @Bean
        @Primary
        public Clock myClock() {
            return Clock.fixed(EPOCH, ZoneId.of("Asia/Kolkata"));
        }
    }
}
