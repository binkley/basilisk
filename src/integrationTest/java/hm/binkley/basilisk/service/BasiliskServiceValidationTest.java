package hm.binkley.basilisk.service;

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
@SpringBootTest(classes = BasiliskService.class)
class BasiliskServiceValidationTest {
    private final BasiliskService service;

    @Test
    void shouldAcceptMinLengthWords() {
        service.extra("A".repeat(3));
    }

    @Test
    void shouldAcceptMaxLengthWords() {
        service.extra("A".repeat(32));
    }

    @Test
    void shouldRejectMissingWords() {
        assertThatThrownBy(this::workaroundForSpotBugs724)
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldRejectShortWords() {
        assertThatThrownBy(() -> service.extra("A".repeat(2)))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldRejectLongWords() {
        assertThatThrownBy(() -> service.extra("A".repeat(33)))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @SuppressFBWarnings("RV")
    private void workaroundForSpotBugs724() {
        service.extra(null);
    }
}
