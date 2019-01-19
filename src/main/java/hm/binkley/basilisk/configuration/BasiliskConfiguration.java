package hm.binkley.basilisk.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static com.google.common.base.Predicates.not;
import static springfox.documentation.builders.RequestHandlerSelectors.basePackage;
import static springfox.documentation.spi.DocumentationType.SWAGGER_2;

@Configuration
@EnableConfigurationProperties(SwaggerProperties.class)
@EnableSwagger2
public class BasiliskConfiguration {
    @Bean
    public ApiInfo apiInfo(final SwaggerProperties swagger) {
        final var contact = swagger.getContact();

        return new ApiInfoBuilder()
                .description(swagger.getDescription())
                .contact(new Contact(
                        contact.getName(),
                        null == contact.getUrl() ? null
                                : contact.getUrl().toString(),
                        contact.getEmail()))
                .license(swagger.getLicense())
                .licenseUrl(null == swagger.getLicenseUrl()
                        ? null : swagger.getLicenseUrl().toString())
                .title(swagger.getTitle())
                .version(swagger.getVersion())
                .build();
    }

    @Bean
    public Docket docket(final ApiInfo apiInfo) {
        return new Docket(SWAGGER_2)
                .apiInfo(apiInfo)
                .select()
                .apis(not(basePackage("org.springframework.boot")))
                .build();
    }
}
