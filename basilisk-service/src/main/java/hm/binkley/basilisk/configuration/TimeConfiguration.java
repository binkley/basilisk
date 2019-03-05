package hm.binkley.basilisk.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;

import static java.time.Clock.systemUTC;

@Configuration
public class TimeConfiguration {
    @Bean
    public Clock systemClockUtc() {
        return systemUTC();
    }
}
