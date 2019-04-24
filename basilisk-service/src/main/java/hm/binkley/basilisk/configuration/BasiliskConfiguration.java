package hm.binkley.basilisk.configuration;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static org.springframework.context.annotation.AdviceMode.ASPECTJ;

@Configuration
@EnableAdminServer
@EnableTransactionManagement(mode = ASPECTJ)
public class BasiliskConfiguration {
}
