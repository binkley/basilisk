package hm.binkley.basilisk.service;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@Import(ValidationAutoConfiguration.class)
@SpringBootTest(classes = BasiliskService.class)
class BasiliskServiceValidationTest {
    @Autowired
    private BasiliskService service;

    private static String wordOfLength(final int length) {
        final var s = new StringBuilder();
        for (int i = 0; i < length; ++i)
            s.append('A');
        return s.toString();
    }

    @Test
    void shouldAcceptMinLengthWords() {
        service.extra(wordOfLength(3));
    }

    @Test
    void shouldAcceptMaxLengthWords() {
        service.extra(wordOfLength(32));
    }

    @Test
    void shouldRejectMissingWords() {
        assertThatThrownBy(this::workaroundForSpotBugs724)
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldRejectShortWords() {
        assertThatThrownBy(() -> service.extra(wordOfLength(2)))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    void shouldRejectLongWords() {
        assertThatThrownBy(() -> service.extra(wordOfLength(33)))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @SuppressFBWarnings("RV")
    private void workaroundForSpotBugs724() {
        service.extra(null);
    }
}
