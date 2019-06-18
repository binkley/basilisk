package hm.binkley.basilisk.ops;

import hm.binkley.basilisk.ops.AlertMessage.Severity;
import lombok.experimental.UtilityClass;

import static hm.binkley.basilisk.ops.AlertMessage.MessageFinder.findAlertMessage;
import static org.assertj.core.api.Assertions.assertThat;

@UtilityClass
public class AlertAssertions {
    public static void assertThatAlertMessage(final Throwable t,
            final String message, final Severity severity) {
        final var alertMessage = findAlertMessage(t);
        assertThat(alertMessage).isNotNull();
        assertThat(alertMessage.message()).isEqualTo(message);
        assertThat(alertMessage.severity()).isEqualTo(severity);
    }
}
