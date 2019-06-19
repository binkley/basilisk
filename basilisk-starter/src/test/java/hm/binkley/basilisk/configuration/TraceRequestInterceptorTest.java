package hm.binkley.basilisk.configuration;

import brave.Span;
import brave.Tracing;
import brave.propagation.TraceContext;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.MDC;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class TraceRequestInterceptorTest {
    private static final String traceIdHeader = "X-B3-TraceId";
    private static final String spanIdHeader = "X-B3-SpanId";

    @Mock
    private final Logger logger;

    @BeforeEach
    void setUp() {
        MDC.clear();
    }

    @Test
    void shouldReuseContextFromMdc() {
        final var traceId = "abcdef0987654321";
        MDC.put(traceIdHeader, traceId);
        MDC.put(spanIdHeader, traceId);

        final var tracing = Tracing.newBuilder().build();
        final var tracer = spy(tracing.tracer());

        final var interceptor = new TraceRequestInterceptor(
                tracing, tracer, logger);
        final var template = spy(new RequestTemplate());

        interceptor.apply(template);

        verify(tracer, never()).newTrace();
        verify(template).header(traceIdHeader, traceId);
    }

    @Test
    void shouldReuseContextFromRequest() {
        final var traceId = "abcdef0987654321";
        final var tracing = Tracing.newBuilder().build();
        final var tracer = spy(tracing.tracer());

        final var interceptor = new TraceRequestInterceptor(
                tracing, tracer, logger);
        final var existingTemplate = new RequestTemplate();
        existingTemplate.header(traceIdHeader, traceId);
        existingTemplate.header(spanIdHeader, traceId);
        final var template = spy(existingTemplate);

        interceptor.apply(template);

        verify(tracer, never()).newTrace();
        verify(template, never()).header(eq(traceIdHeader), anyString());
    }

    @Test
    void shouldReuseContextProvided() {
        final var traceId = "abcdef0987654321";
        final var tracing = Tracing.newBuilder().build();
        final var tracer = spy(tracing.tracer());
        final var currentSpan = mock(Span.class);
        doReturn(currentSpan)
                .when(tracer).currentSpan();
        final var currentContext = mock(TraceContext.class);
        doReturn(currentContext)
                .when(currentSpan).context();
        // And so the problems with mocks: A little too much knowledge about
        // the innards of the mocked class
        doReturn(traceId)
                .when(currentContext).traceIdString();
        doReturn(traceId)
                .when(currentContext).spanIdString();

        final var interceptor = new TraceRequestInterceptor(
                tracing, tracer, logger);
        final var template = spy(new RequestTemplate());

        interceptor.apply(template);

        verify(tracer, never()).newTrace();
        verify(template).header(traceIdHeader, traceId);
    }

    @Test
    void shouldCreateContext() {
        final var tracing = Tracing.newBuilder().build();
        final var tracer = spy(tracing.tracer());

        final var interceptor = new TraceRequestInterceptor(
                tracing, tracer, logger);
        final var template = spy(new RequestTemplate());

        interceptor.apply(template);

        verify(tracer).newTrace();
        verify(template).header(eq(traceIdHeader), anyString());
    }
}
