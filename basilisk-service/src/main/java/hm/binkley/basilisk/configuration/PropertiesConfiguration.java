package hm.binkley.basilisk.configuration;

import hm.binkley.basilisk.flora.configuration.FloraProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        BasiliskProperties.class,
        FloraProperties.class,
        OverlappedProperties.class})
public class PropertiesConfiguration {
}
