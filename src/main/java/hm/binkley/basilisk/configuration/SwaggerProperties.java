package hm.binkley.basilisk.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import java.net.URI;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PUBLIC;

// Note all the annotations -- configuration properties has one set of
// requirements; clean testing has another set: accommodate them both
@AllArgsConstructor(access = PRIVATE)
@Builder
@ConfigurationProperties("swagger.ui")
@Data
@NoArgsConstructor(access = PUBLIC)
@Validated
public class SwaggerProperties {
    private String description;
    @Builder.Default
    private Contact contact = new Contact();
    private String license;
    private URI licenseUrl;
    private String title;
    private String version;

    @AllArgsConstructor(access = PRIVATE)
    @Builder
    @Data
    @NoArgsConstructor(access = PUBLIC)
    @Validated
    public static class Contact {
        private String name;
        private URI url;
        private @Email String email;
    }
}
