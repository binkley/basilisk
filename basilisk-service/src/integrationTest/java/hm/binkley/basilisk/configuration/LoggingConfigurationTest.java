package hm.binkley.basilisk.configuration;

import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
@Import(LoggingConfiguration.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class LoggingConfigurationTest {
    private final Logger logger;

    /**
     * Notes: <ul>
     * <li>Inject a logger into the test itself, rather than another
     * class</li>
     * <li>Logback has a custom interface, extending plain {@code
     * Logger}, requiring casts</li>
     * <li>There is no equality check for the logging event</li>
     * <li>Mocking prevents us from getting at the point of the test,
     * necessitating a spy</li>
     * <li>The {@code argThat} technique to avoid argument capture</li>
     * </ul>
     *
     * @todo This is a complex test; can it be done simpler? Note:
     */
    @Test
    void shouldInjectLoggerSpecificToInjectedClass() {
        final var format = "Foo";
        final var logger = spy(this.logger);
        final var logbackLogger = (ch.qos.logback.classic.Logger) logger;
        doNothing().when(logbackLogger)
                .callAppenders(any(ILoggingEvent.class));

        logger.error(format);

        verify(logbackLogger).callAppenders(argThat(e ->
                e.getLoggerName().equals(getClass().getName())));
    }
}
