package hm.binkley.basilisk;

import feign.Request;
import feign.Response;
import feign.Response.Body;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.zalando.logbook.Logbook;

import java.io.ByteArrayInputStream;
import java.io.IOException;

import static feign.Logger.Level.FULL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Answers.RETURNS_MOCKS;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class LogbookFeignLoggerTest {
    @Mock
    private final Logbook logbook;
    @Mock
    private final Logger slf4jLogger;

    private LogbookFeignLogger feignLogger;

    @BeforeEach
    void setUp() {
        feignLogger = new LogbookFeignLogger(logbook, slf4jLogger);
    }

    /**
     * These must be tested together, making the thread-local context
     * available to the response: they are not independent.
     */
    @Test
    void shouldLogRequestAndResponseOnce()
            throws IOException {
        feignLogger.logRequest("FOO", FULL,
                mock(Request.class, RETURNS_MOCKS));

        verify(logbook).write(any());

        final var response = mock(Response.class, RETURNS_MOCKS);
        final var body = mock(Body.class, RETURNS_MOCKS);
        when(response.body())
                .thenReturn(body);
        when(body.asInputStream())
                .thenReturn(new ByteArrayInputStream(new byte[0]));

        feignLogger.logAndRebufferResponse("FOO", FULL, response, 1L);

        verify(logbook).write(any());
        verifyNoMoreInteractions(logbook, slf4jLogger);
    }

    @Test
    void shouldLogIOExceptionsOnceAndAsError() {
        final var configKey = "FOO";
        final var ioe = new IOException();
        final var elapsedTime = 1L;

        final var value = feignLogger.logIOException(configKey, FULL, ioe,
                elapsedTime);

        assertThat(value).isSameAs(ioe);

        // TODO: How to verify AND ignore the parameters?
        verify(slf4jLogger).error(
                anyString(), eq(configKey), eq(elapsedTime),
                eq(ioe.toString()), eq(ioe));
        verifyNoMoreInteractions(logbook, slf4jLogger);
    }

    @Test
    void shouldLogRetriesOnceAndAsWarning() {
        final var configKey = "FOO";

        feignLogger.logRetry(configKey, FULL);

        verify(slf4jLogger).warn(anyString(), eq(configKey));
        verifyNoMoreInteractions(logbook, slf4jLogger);
    }
}
