package hm.binkley.basilisk;

import org.springframework.boot.context.event.ApplicationStartingEvent;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.event.SmartApplicationListener;

public class QuietApplicationListener
        implements SmartApplicationListener {
    @Override
    public boolean supportsEventType(
            final Class<? extends ApplicationEvent> eventType) {
        return ApplicationStartingEvent.class.isAssignableFrom(eventType);
    }

    @Override
    public void onApplicationEvent(final ApplicationEvent event) {
        final var startingEvent = (ApplicationStartingEvent) event;
        startingEvent.getSpringApplication().setLogStartupInfo(false);
    }
}
