package hm.binkley.basilisk.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolationException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
class BasiliskServiceTest {
    @Autowired
    private BasiliskService service;

    @Test
    void shouldAcceptWords() {
        assertThat(service.extra("foo")).isEqualTo("Uncle Bob");
    }

    @Test
    void shouldRejectMissingWords() {
        assertThatThrownBy(() -> service.extra(null))
                .isInstanceOf(ConstraintViolationException.class);
    }
}
