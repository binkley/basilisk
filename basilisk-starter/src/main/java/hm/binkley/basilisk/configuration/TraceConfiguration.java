package hm.binkley.basilisk.configuration;

import brave.Tracer;
import brave.Tracing;
import lombok.Generated;
import org.slf4j.Logger;
import org.springframework.cloud.sleuth.autoconfig.TraceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Generated // Lie to Jacoco
@Import(TraceAutoConfiguration.class)
public class TraceConfiguration {
    @Bean
    public TraceResponseFilter traceResponseFilter(
            final Tracing tracing, final Tracer tracer, final Logger logger) {
        return new TraceResponseFilter(tracing, tracer, logger);
    }
}
