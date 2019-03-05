package hm.binkley.basilisk;

import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationStartingEvent;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class QuietApplicationListenerTest {
    @Test
    void shouldSuppressStartedLogging() {
        final var application = mock(SpringApplication.class);
        new QuietApplicationListener().onApplicationEvent(
                new ApplicationStartingEvent(application, new String[0]));

        verify(application).setLogStartupInfo(false);
    }
}
