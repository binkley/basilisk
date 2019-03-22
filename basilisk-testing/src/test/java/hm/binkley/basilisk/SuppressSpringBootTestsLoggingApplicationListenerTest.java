package hm.binkley.basilisk;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.event.ApplicationStartingEvent;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.springframework.boot.Banner.Mode.OFF;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class SuppressSpringBootTestsLoggingApplicationListenerTest {
    private final SuppressSpringBootTestsLoggingApplicationListener listener
            = new SuppressSpringBootTestsLoggingApplicationListener();

    @Mock
    private final SpringApplication application;

    private ApplicationStartingEvent event;

    @BeforeEach
    void setUp() {
        event = new ApplicationStartingEvent(application, new String[0]);
    }

    @Test
    void shouldSuppressApplicationStartedLogging() {
        listener.onApplicationEvent(event);

        verify(application).setLogStartupInfo(false);
    }

    @Test
    void shouldSuppressBanner() {
        listener.onApplicationEvent(event);

        verify(application).setBannerMode(OFF);
    }

    @Test
    void shouldSupportApplicationStartingEvent() {
        assertThat(listener
                .supportsEventType(ApplicationStartingEvent.class))
                .withFailMessage("Does not support "
                        + ApplicationStartingEvent.class.getSimpleName())
                .isTrue();
    }
}
