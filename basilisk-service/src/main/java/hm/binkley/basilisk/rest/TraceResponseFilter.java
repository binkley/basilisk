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

        chain.doFilter(request, response);

        addTraceHeaders(request, span.context(), response);
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis") // TODO: Why?
    private void addTraceHeaders(final HttpServletRequest request,
            final TraceContext context, final HttpServletResponse response) {
        for (final SleuthHeader header : SleuthHeader.values()) {
            header.addTo(request, response, context);
        }
    }

    // Grrr ... why aren't these public constants from Brave or Sleuth?
    @RequiredArgsConstructor
    public enum SleuthHeader {
        TraceId(TraceContext::traceIdString) {
            @Override
            void addTo(final HttpServletRequest request,
                    final HttpServletResponse response,
                    final TraceContext context) {
                final var requestValue = request.getHeader(toString());
                final var traceValue = read.apply(context);
                response.setHeader(toString(), null == requestValue
                        ? String.valueOf(traceValue)
                        : requestValue);
            }
        },
        SpanId(TraceContext::spanIdString),
        ParentSpanId(TraceContext::parentIdString),
        Sampled(TraceContext::sampled),
        Flags(TraceResponseFilter::flags);

        protected final Function<TraceContext, Object> read;

        void addTo(final HttpServletRequest request,
                final HttpServletResponse response,
                final TraceContext context) {
            final var traceValue = read.apply(context);
            response.setHeader(toString(),
                    null == traceValue ? null : String.valueOf(traceValue));
        }

        @Override
        public String toString() {
            return "X-B3-" + name();
        }
    }
}
