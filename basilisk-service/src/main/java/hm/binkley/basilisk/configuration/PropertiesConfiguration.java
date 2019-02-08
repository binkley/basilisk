package hm.binkley.basilisk.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        BasiliskProperties.class,
        OverlappedProperties.class})
public class PropertiesConfiguration {
}
