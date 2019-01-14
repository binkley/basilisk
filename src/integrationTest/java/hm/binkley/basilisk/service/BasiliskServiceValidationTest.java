package hm.binkley.basilisk.service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class BasiliskServiceValidationTest {
    @Autowired
    private BasiliskService service;

    @Test
    void shouldRejectMissingWords() {
        assertThatThrownBy(this::workaroundForSpotBugs724)
                .isInstanceOf(ConstraintViolationException.class);
    }

    @SuppressFBWarnings("RV")
    private void workaroundForSpotBugs724() {
        service.extra(null);
    }
}
