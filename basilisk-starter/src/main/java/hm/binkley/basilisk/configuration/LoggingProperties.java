package hm.binkley.basilisk.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("logging")
@Data
public class LoggingProperties {
    private boolean logFeignRetries;
}
