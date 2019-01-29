package hm.binkley.basilisk.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import java.net.URI;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PUBLIC;

// Note all the annotations -- configuration properties has one set of
// requirements; clean testing has another set: accommodate them both
@AllArgsConstructor(access = PRIVATE)
@Builder
@ConfigurationProperties("basilisk.overlapped")
@Data
@NoArgsConstructor(access = PUBLIC)
@Validated
public class OverlappedProperties {
    private URI endpointBase;
}
