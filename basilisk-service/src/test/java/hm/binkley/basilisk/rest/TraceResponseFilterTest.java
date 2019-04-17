package hm.binkley.basilisk.rest;

import brave.Span;
import brave.Tracer;
import brave.propagation.TraceContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static hm.binkley.basilisk.rest.TraceResponseFilter.SleuthHeader.Flags;
import static hm.binkley.basilisk.rest.TraceResponseFilter.SleuthHeader.ParentSpanId;
import static hm.binkley.basilisk.rest.TraceResponseFilter.SleuthHeader.Sampled;
import static hm.binkley.basilisk.rest.TraceResponseFilter.SleuthHeader.SpanId;
import static hm.binkley.basilisk.rest.TraceResponseFilter.SleuthHeader.TraceId;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TraceResponseFilterTest {
    @Mock
    private Tracer tracer;
    @Mock
    private FilterChain chain;

    private TraceResponseFilter filter;

    @BeforeEach
    void setUp() {
        filter = new TraceResponseFilter(tracer);
    }

    @Test
    void shouldPreserveExistingTraceId()
            throws IOException, ServletException {
        final var traceId = "1234";
        final var request = new MockHttpServletRequest();
        request.addHeader(TraceId.toString(), traceId);
        final var response = mock(HttpServletResponse.class);
        final var span = mock(Span.class);
        when(tracer.currentSpan())
                .thenReturn(span);
        final TraceContext context = mock(TraceContext.class);
        when(span.context())
                .thenReturn(context);

        filter.doFilter(request, response, chain);

        verify(response).setHeader(TraceId.toString(), traceId);
    }

    @Test
    void shouldContinueCallingTheChainWithoutASpan()
            throws IOException, ServletException {
        final var request = new MockHttpServletRequest();
        final var response = new MockHttpServletResponse();

        filter.doFilter(request, response, chain);

        verify(chain).doFilter(request, response);
    }

    @Test
    void shouldPassDebuggingInFlagsHeader()
            throws IOException, ServletException {
        final var request = new MockHttpServletRequest();
        final var response = mock(MockHttpServletResponse.class);
        final var span = mock(Span.class);
        when(tracer.currentSpan())
                .thenReturn(span);
        final TraceContext context = mock(TraceContext.class);
        when(span.context())
                .thenReturn(context);
        when(context.debug())
                .thenReturn(true);

        filter.doFilter(request, response, chain);

        verify(response).setHeader(Flags.toString(), "1");
        verify(chain).doFilter(request, response);
    }

    @Test
    void shouldPassNotDebuggingInFlagsHeader()
            throws IOException, ServletException {
        final var request = new MockHttpServletRequest();
        final var response = mock(HttpServletResponse.class);
        final var span = mock(Span.class);
        when(tracer.currentSpan())
                .thenReturn(span);
        final TraceContext context = mock(TraceContext.class);
        when(span.context())
                .thenReturn(context);
        when(context.debug())
                .thenReturn(false);

        filter.doFilter(request, response, chain);

        verify(response).setHeader(Flags.toString(), "0");
        verify(chain).doFilter(request, response);
    }

    @Test
    void shouldTurnIntoProperTracingHeaders() {
        assertThat(ParentSpanId.toString()).isEqualTo("X-B3-ParentSpanId");
        assertThat(SpanId.toString()).isEqualTo("X-B3-SpanId");
        assertThat(TraceId.toString()).isEqualTo("X-B3-TraceId");
        assertThat(Sampled.toString()).isEqualTo("X-B3-Sampled");
        assertThat(Flags.toString()).isEqualTo("X-B3-Flags");
    }
}
