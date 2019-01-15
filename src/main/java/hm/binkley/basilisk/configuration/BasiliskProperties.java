package hm.binkley.basilisk.configuration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PUBLIC;

// Note all the annotations -- configuration properties has one set of
// requirements; clean testing has another set: accomodate them both
@AllArgsConstructor(access = PRIVATE)
@Builder
@ConfigurationProperties("basilisk")
@Data
@NoArgsConstructor(access = PUBLIC)
public class BasiliskProperties {
    private String extraWord;
}
