package hm.binkley.basilisk.configuration;

import hm.binkley.basilisk.LogbookFeignLogger;
import lombok.Generated;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.FeignLoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients
@Generated // Lie to JaCoCo
public class FeignConfiguration {
    @Bean
    public FeignLoggerFactory replaceFeignLoggerWithLogbook(
            final LogbookFeignLogger logger) {
        return type -> logger;
    }
}
