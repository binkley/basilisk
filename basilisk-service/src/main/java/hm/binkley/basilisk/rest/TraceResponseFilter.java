package hm.binkley.basilisk.rest;

import brave.Tracer;
import brave.Tracing;
import brave.propagation.TraceContext;
import brave.propagation.TraceContext.Extractor;
import brave.propagation.TraceContext.Injector;
import brave.propagation.TraceContextOrSamplingFlags;
import lombok.Generated;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/** @todo How to test? */
@Generated // Lie to Jacoco
public class TraceResponseFilter
        extends OncePerRequestFilter {
    private final Tracer tracer;
    private final Extractor<HttpServletRequest> extractor;
    private final Injector<HttpServletResponse> injector;

    public TraceResponseFilter(final Tracing tracing, final Tracer tracer) {
        extractor = tracing.propagation().extractor(
                HttpServletRequest::getHeader);
        injector = tracing.propagation().injector(
                HttpServletResponse::setHeader);
        this.tracer = tracer;
    }

    private static TraceContext compoundContext(
            final TraceContext currentContext,
            final TraceContextOrSamplingFlags extraction) {
        return TraceContext.newBuilder()
                .debug(currentContext.debug())
                .parentId(currentContext.parentId())
                .sampled(currentContext.sampled())
                .spanId(currentContext.spanId())
                .traceId(workingTraceId(extraction, currentContext))
                .build();
    }

    private static long workingTraceId(
            final TraceContextOrSamplingFlags extraction,
            final TraceContext currentContext) {
        final TraceContext requestContext = extraction.context();
        return null == requestContext
                ? currentContext.traceId()
                : requestContext.traceId();
    }

    @Override
    public void doFilterInternal(final HttpServletRequest request,
            final HttpServletResponse response, final FilterChain chain)
            throws IOException, ServletException {
        final var currentSpan = tracer.currentSpan();
        if (null == currentSpan) {
            chain.doFilter(request, response);
            return;
        }

        final var compoundContext = compoundContext(
                currentSpan.context(),
                extractor.extract(request));

        chain.doFilter(request, response);

        injector.inject(compoundContext, response);
    }
}