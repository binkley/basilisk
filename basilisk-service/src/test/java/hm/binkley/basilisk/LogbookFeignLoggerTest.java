package hm.binkley.basilisk;

import feign.Request;
import feign.Response;
import feign.Response.Builder;
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
import java.util.List;
import java.util.Map;

import static feign.Logger.Level.FULL;
import static feign.Request.HttpMethod.GET;
import static org.assertj.core.api.Assertions.assertThat;
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
    private static final String configKey = "FOO";

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
    void shouldLogRequestAndResponseWithBodyOnce()
            throws IOException {
        final var request = mock(Request.class);
        final var requestBody = mock(Request.Body.class);
        when(request.httpMethod())
                .thenReturn(GET);
        when(request.url())
                .thenReturn("http://some/where");
        when(request.headers())
                .thenReturn(Map.of("A", List.of("1", "2")));
        when(request.requestBody())
                .thenReturn(requestBody);
        final var bodyData = new byte[]{'a', 'b', 'c'};
        when(requestBody.asBytes())
                .thenReturn(bodyData);
        feignLogger.logRequest(configKey, FULL,
                request);

        verify(logbook).write(any());

        final var response = mock(Response.class);
        final var responseBody = mock(Response.Body.class);
        when(response.status())
                .thenReturn(1);
        when(response.reason())
                .thenReturn("Cool");
        when(response.headers())
                .thenReturn(Map.of("B", List.of("3", "4")));
        when(response.body())
                .thenReturn(responseBody);
        when(responseBody.asInputStream())
                .thenReturn(new ByteArrayInputStream(bodyData));
        final Builder responseBuilder = mock(Builder.class);
        when(response.toBuilder())
                .thenReturn(responseBuilder);
        when(responseBuilder.body(bodyData))
                .thenReturn(responseBuilder);
        final var returnedResponse = mock(Response.class);
        when(responseBuilder.build())
                .thenReturn(returnedResponse);

        final var value = feignLogger
                .logAndRebufferResponse(configKey, FULL, response, 1L);

        assertThat(value).isSameAs(returnedResponse);

        verify(logbook).write(any());
        verifyNoMoreInteractions(logbook, slf4jLogger);
    }

    @Test
    void shouldLogRequestAndResponseWithNoBodyOnce()
            throws IOException {
        final var request = mock(Request.class);
        final var requestBody = mock(Request.Body.class);
        when(request.httpMethod())
                .thenReturn(GET);
        when(request.url())
                .thenReturn("http://some/where");
        when(request.headers())
                .thenReturn(Map.of("A", List.of("1", "2")));
        when(request.requestBody())
                .thenReturn(requestBody);
        final var bodyData = new byte[0];
        when(requestBody.asBytes())
                .thenReturn(bodyData);
        feignLogger.logRequest(configKey, FULL,
                request);

        verify(logbook).write(any());

        final var response = mock(Response.class);
        final Response.Body responseBody = null;
        when(response.status())
                .thenReturn(1);
        when(response.reason())
                .thenReturn("Cool");
        when(response.headers())
                .thenReturn(Map.of("B", List.of("3", "4")));
        when(response.body())
                .thenReturn(responseBody);
        final Builder responseBuilder = mock(Builder.class);
        when(response.toBuilder())
                .thenReturn(responseBuilder);
        when(responseBuilder.body(bodyData))
                .thenReturn(responseBuilder);
        final var returnedResponse = mock(Response.class);
        when(responseBuilder.build())
                .thenReturn(returnedResponse);

        final var value = feignLogger
                .logAndRebufferResponse(configKey, FULL, response, 1L);

        assertThat(value).isSameAs(returnedResponse);

        verify(logbook).write(any());
        verifyNoMoreInteractions(logbook, slf4jLogger);
    }

    @Test
    void shouldLogIOExceptionsOnceAndAsError() {
        final var ioe = new IOException();
        final var elapsedTime = 1L;

        final var value = feignLogger.logIOException(
                configKey, FULL, ioe, elapsedTime);

        assertThat(value).isSameAs(ioe);

        // TODO: How to verify AND ignore the parameters?
        verify(slf4jLogger).error(
                anyString(), eq(configKey), eq(elapsedTime),
                eq(ioe.toString()), eq(ioe));
        verifyNoMoreInteractions(logbook, slf4jLogger);
    }

    @Test
    void shouldLogRetriesOnceAndAsWarning() {
        feignLogger.logRetry(configKey, FULL);

        verify(slf4jLogger).warn(anyString(), eq(configKey));
        verifyNoMoreInteractions(logbook, slf4jLogger);
    }
}
