package hm.binkley.basilisk.configuration;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.slf4j.Logger;
import org.springframework.beans.factory.InjectionPoint;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import static java.util.Objects.requireNonNull;
import static org.slf4j.LoggerFactory.getLogger;
import static org.springframework.beans.factory.config.ConfigurableBeanFactory.SCOPE_PROTOTYPE;

@Configuration
@EnableConfigurationProperties(LoggingProperties.class)
public class LoggingConfiguration {
    @Bean
    @Scope(SCOPE_PROTOTYPE)
    @SuppressFBWarnings("NP")
    public Logger logger(final InjectionPoint at) {
        return getLogger(requireNonNull(at.getMethodParameter())
                .getContainingClass());
    }
}
