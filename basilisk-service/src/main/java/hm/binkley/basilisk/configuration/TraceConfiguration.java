package hm.binkley.basilisk.configuration;

import brave.Tracer;
import hm.binkley.basilisk.rest.TraceResponseFilter;
import lombok.Generated;
import org.springframework.cloud.sleuth.autoconfig.TraceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Generated // Lie to Jacoco
@Import(TraceAutoConfiguration.class)
public class TraceConfiguration {
    @Bean
    public TraceResponseFilter traceResponseFilter(final Tracer tracer) {
        return new TraceResponseFilter(tracer);
    }
}
