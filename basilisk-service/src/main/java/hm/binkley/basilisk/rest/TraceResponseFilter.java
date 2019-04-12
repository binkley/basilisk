package hm.binkley.basilisk.rest;

import brave.Span;
import brave.Tracer;
import brave.propagation.B3Propagation;
import brave.propagation.TraceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Function;

/**
 * @todo Use {@link B3Propagation} injector and extractor, rather than doing
 * this manually
 */
@RequiredArgsConstructor
public class TraceResponseFilter
        extends GenericFilterBean {
    private final Tracer tracer;

    private static String flags(final TraceContext context) {
        // Look at B3Propagation.B3Injector.inject
        return context.debug() ? "1" : "0";
    }

    @Override
    public void doFilter(final ServletRequest request,
            final ServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        final Span span = tracer.currentSpan();
        if (null == span) {
            chain.doFilter(request, response);
            return;
        }

        addTraceHeaders((HttpServletResponse) response, span.context());

        chain.doFilter(request, response);
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis") // TODO: Why?
    private void addTraceHeaders(final HttpServletResponse httpResponse,
            final TraceContext context) {
        for (final SleuthHeader header : SleuthHeader.values()) {
            header.addTo(httpResponse, context);
        }
    }

    // Grrr ... why aren't these public constants from Brave or Sleuth?
    @RequiredArgsConstructor
    public enum SleuthHeader {
        TraceId(TraceContext::traceIdString),
        SpanId(TraceContext::spanIdString),
        ParentSpanId(TraceContext::parentIdString),
        Sampled(TraceContext::sampled),
        Flags(TraceResponseFilter::flags);

        private final Function<TraceContext, Object> read;

        void addTo(final HttpServletResponse response,
                final TraceContext context) {
            final Object value = read.apply(context);
            if (null != value) {
                response.addHeader(toString(), value.toString());
            }
        }

        @Override
        public String toString() {
            return "X-B3-" + name();
        }
    }
}
