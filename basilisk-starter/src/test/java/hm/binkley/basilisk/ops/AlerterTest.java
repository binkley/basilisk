package hm.binkley.basilisk.ops;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;

import java.util.LinkedHashMap;

import static hm.binkley.basilisk.ops.AlertMessage.Severity.HIGH;
import static hm.binkley.basilisk.ops.AlertMessage.Severity.LOW;
import static hm.binkley.basilisk.ops.AlertMessage.Severity.MEDIUM;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class AlerterTest {
    private static final String message = "HI, MOM!";

    @Mock
    private final Logger logger;

    private Alerter alerter;

    @BeforeEach
    void setUp() {
        alerter = new Alerter(logger);
    }

    @Test
    void shouldAlertHigh() {
        alerter.alertHigh(message);

        then(logger).should().error(anyString(), eq(HIGH), eq(message));
    }

    @Test
    void shouldAlertMedium() {
        alerter.alertMedium(message);

        then(logger).should().error(anyString(), eq(MEDIUM), eq(message));
    }

    @Test
    void shouldAlertLow() {
        alerter.alertLow(message);

        then(logger).should().error(anyString(), eq(LOW), eq(message));
    }

    @Test
    void shouldAlertWithAnnotation() {
        final AlertMessage annotation = mock(AlertMessage.class);
        given(annotation.message())
                .willReturn(message);
        given(annotation.severity())
                .willReturn(MEDIUM);

        // Preserve order
        final var extra = new LinkedHashMap<String, Object>();
        extra.put("key1", "value1");
        extra.put("key2", "value2");

        alerter.alert(annotation, extra);

        then(logger).should().error(
                anyString(), eq(MEDIUM), eq(message),
                eq("key1=value1;key2=value2"));
    }
}
