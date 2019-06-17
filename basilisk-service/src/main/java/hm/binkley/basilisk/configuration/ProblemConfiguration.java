package hm.binkley.basilisk.configuration;

import lombok.Generated;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

import static hm.binkley.basilisk.configuration.ExceptionHandling.includeStackTrace;

@Configuration
@EnableAutoConfiguration(exclude = ErrorMvcAutoConfiguration.class)
@Generated // Lie to JaCoCo
public class ProblemConfiguration {
    @Bean
    public ProblemModule problemModule(final ServerProperties server) {
        return new ProblemModule()
                .withStackTraces(includeStackTrace(server));
    }

    @Bean
    public ConstraintViolationProblemModule constraintViolationProblemModule() {
        return new ConstraintViolationProblemModule();
    }
}
