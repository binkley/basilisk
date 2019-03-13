package hm.binkley.basilisk.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.auditing.DateTimeProvider;
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing;

import java.time.Clock;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Optional;

import static java.time.ZoneOffset.UTC;

@Configuration
@EnableJdbcAuditing
public class DatabaseConfiguration {
    @Bean
    public Clock clock() {
        return Clock.systemDefaultZone();
    }

    @Bean
    public DateTimeProvider utcDateTimeProvider(final Clock clock) {
        return () -> Optional.of(
                ZonedDateTime.ofInstant(Instant.now(clock), UTC));
    }
}
