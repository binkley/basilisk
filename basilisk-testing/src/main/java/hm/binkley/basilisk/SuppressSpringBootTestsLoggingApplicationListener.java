package hm.binkley.basilisk;

import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;

import static org.springframework.boot.Banner.Mode.OFF;

public class SuppressSpringBootTestsLoggingApplicationListener
        implements SmartApplicationListener {
    @Override
    public boolean supportsEventType(
            final Class<? extends ApplicationEvent> eventType) {
        return ApplicationStartingEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public void onApplicationEvent(final ApplicationEvent event) {
        final var application = ((ApplicationStartingEvent) event)
                .getSpringApplication();
        application.setBannerMode(OFF);
        application.setLogStartupInfo(false);
    }
}
