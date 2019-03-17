package hm.binkley.basilisk.flora.service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import(ValidationAutoConfiguration.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SpringBootTest(classes = SpecialService.class)
class SpecialServiceValidationTest {
    private final SpecialService service;

    @Test
    void shouldRejectMissingRecipe() {
        assertThatThrownBy(this::workaroundForSpotBugs724)
                .isInstanceOf(ConstraintViolationException.class);
    }

    @SuppressFBWarnings("RV")
    private void workaroundForSpotBugs724() {
        service.isDailySpecial(null);
    }
}
