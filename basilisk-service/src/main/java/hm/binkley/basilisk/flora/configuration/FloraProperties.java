package hm.binkley.basilisk.flora.configuration;

import hm.binkley.basilisk.configuration.OverlappedProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import static lombok.AccessLevel.PRIVATE;
import static lombok.AccessLevel.PUBLIC;

// Note all the annotations -- configuration properties has one set of
// requirements; clean testing has another set: accommodate them both
@AllArgsConstructor(access = PRIVATE)
@Builder
@ConfigurationProperties("flora")
@Data
@NoArgsConstructor(access = PUBLIC)
@Validated
public class FloraProperties {
    private @Length(min = 3, max = 32) String dailySpecial;
    private Nested nested;
    private OverlappedProperties overlapped;

    @Data
    public static class Nested {
        private int number;
    }
}
