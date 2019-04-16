package hm.binkley.basilisk.rest;

import brave.Span;
import brave.Tracer;
import brave.propagation.B3Propagation;
import brave.propagation.TraceContext;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.RequiredArgsConstructor;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.function.Function;

import static hm.binkley.basilisk.rest.TraceResponseFilter.SleuthHeader.TraceId;

/**
 * @todo Use {@link B3Propagation} injector and extractor, rather than doing
 * this manually
 */
@RequiredArgsConstructor
@SuppressFBWarnings("HRS_REQUEST_PARAMETER_TO_HTTP_HEADER")
public class TraceResponseFilter
        extends OncePerRequestFilter {
    private final Tracer tracer;

    private static String flags(final TraceContext context) {
        // Look at B3Propagation.B3Injector.inject
        return context.debug() ? "1" : "0";
    }

    @Override
    public void doFilterInternal(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        final Span span = tracer.currentSpan();
        if (null == span) {
            chain.doFilter(request, response);
            return;
        }

        // OK, we _really_ need to use the propagation framework
        TraceId.copyFrom(request, response);
        addTraceHeaders(response, span.context());

        chain.doFilter(request, response);
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis") // TODO: Why?
    private void addTraceHeaders(final HttpServletResponse response,
            final TraceContext context) {
        for (final SleuthHeader header : SleuthHeader.values()) {
            header.addTo(response, context);
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

        void copyFrom(final HttpServletRequest request,
                final HttpServletResponse response) {
            final String requestValue = request.getHeader(toString());
            if (null != requestValue) {
                response.setHeader(toString(), requestValue);
            }
        }

        void addTo(final HttpServletResponse response,
                final TraceContext context) {
            if (null != response.getHeader(toString())) {
                return;
            }

            final Object traceValue = read.apply(context);
            if (null != traceValue) {
                response.addHeader(toString(), traceValue.toString());
            }
        }

        @Override
        public String toString() {
            return "X-B3-" + name();
        }
    }
}
