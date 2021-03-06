package hm.binkley.basilisk.configuration;

import brave.Span;
import brave.Tracing;
import brave.propagation.TraceContext;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.springframework.mock.web.MockFilterChain;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.ServletException;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@RequiredArgsConstructor
class TraceResponseFilterTest {
    private static final String traceIdHeader = "X-B3-TraceId";
    private static final String spanIdHeader = "X-B3-SpanId";

    @Mock
    private final Logger logger;

    @BeforeEach
    void setUp() {
        lenient().doNothing().when(logger).trace(any());
        lenient().doNothing().when(logger).debug(any());
    }

    @Test
    void shouldReuseContext()
            throws ServletException, IOException {
        final var traceId = "deadbeef";
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

        final var filter = new TraceResponseFilter(tracing, tracer, logger);
        final var request = new MockHttpServletRequest();
        final var response = spy(new MockHttpServletResponse());
        final var chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        verify(tracer, never()).newTrace();
        verify(response).setHeader(traceIdHeader, traceId);
    }

    @Test
    void shouldCreateContext()
            throws ServletException, IOException {
        final var tracing = Tracing.newBuilder().build();
        final var tracer = spy(tracing.tracer());

        final var filter = new TraceResponseFilter(tracing, tracer, logger);
        final var request = new MockHttpServletRequest();
        final var response = spy(new MockHttpServletResponse());
        final var chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        verify(tracer).newTrace();
        verify(response).setHeader(eq(traceIdHeader), anyString());
    }

    @Test
    void shouldWarnIfTraceIdInvalid()
            throws ServletException, IOException {
        final var traceId = "deadbeef";
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

        final var filter = new TraceResponseFilter(tracing, tracer, logger);
        final var request = new MockHttpServletRequest();
        final var wrongTraceId = traceId + "AAA";
        request.addHeader(traceIdHeader, wrongTraceId);
        request.addHeader(spanIdHeader, traceId);
        final var response = spy(new MockHttpServletResponse());
        final var chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        verify(logger).warn(anyString(),
                eq(wrongTraceId), eq(traceId), eq(traceId), eq(traceId));
        verify(tracer, never()).newTrace();
        verify(response).setHeader(traceIdHeader, traceId);
        verify(response).setHeader(spanIdHeader, traceId);
    }

    @Test
    void shouldWarnIfSpanIdInvalid()
            throws ServletException, IOException {
        final var traceId = "deadbeef";
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

        final var filter = new TraceResponseFilter(tracing, tracer, logger);
        final var request = new MockHttpServletRequest();
        final var wrongTraceId = traceId + "AAA";
        request.addHeader(traceIdHeader, traceId);
        request.addHeader(spanIdHeader, wrongTraceId);
        final var response = spy(new MockHttpServletResponse());
        final var chain = new MockFilterChain();

        filter.doFilter(request, response, chain);

        verify(logger).warn(anyString(),
                eq(traceId), eq(wrongTraceId), eq(traceId), eq(traceId));
        verify(tracer, never()).newTrace();
        verify(response).setHeader(traceIdHeader, traceId);
        verify(response).setHeader(spanIdHeader, traceId);
    }
}
