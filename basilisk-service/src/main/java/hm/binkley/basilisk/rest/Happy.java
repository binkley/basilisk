package hm.binkley.basilisk.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.Instant;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class Happy
        extends AbstractHealthIndicator {
    private final Clock clock;

    @Override
    protected void doHealthCheck(final Builder builder) {
        builder.withDetails(Map.of(
                "apple", "core",
                "friend?", Instant.now(clock)));
    }
}
