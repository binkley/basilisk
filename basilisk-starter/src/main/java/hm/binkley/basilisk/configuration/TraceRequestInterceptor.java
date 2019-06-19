package hm.binkley.basilisk.configuration;

import brave.Tracer;
import brave.Tracing;
import brave.propagation.TraceContext;
import brave.propagation.TraceContext.Extractor;
import brave.propagation.TraceContext.Injector;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import hm.binkley.basilisk.Bug;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.spi.MDCAdapter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static brave.propagation.TraceContextOrSamplingFlags.EMPTY;
import static org.slf4j.MDC.getMDCAdapter;

@Component
public class TraceRequestInterceptor
        implements RequestInterceptor, TraceMixin {
    private final Tracer tracer;
    private final Extractor<MDCAdapter> mdcExtractor;
    private final Extractor<RequestTemplate> requestExtractor;
    private final Injector<RequestTemplate> injector;
    private final Logger logger;

    @Autowired
    public TraceRequestInterceptor(final Tracing tracing,
            final Tracer tracer, final Logger logger) {
        final var propagation = tracing.propagation();
        mdcExtractor = propagation.extractor(MDCAdapter::get);
        requestExtractor = propagation.extractor((r, k) ->
                firstOf(r.headers().get(k)));
        injector = propagation.injector(RequestTemplate::header);
        this.tracer = tracer;
        this.logger = logger;
    }

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    @Generated // Lie to JaCoCo -- Bug is not realistic
    private static <T> T firstOf(final Iterable<T> its) {
        if (null == its)
            return null;
        for (final T it : its)
            return it;
        throw new Bug("Header exists with no values");
    }

    @Override
    public void apply(final RequestTemplate template) {
        // On retry, the previous trace details are still there
        final var existingTrace = requestExtractor.extract(template);
        if (EMPTY != existingTrace)
            return;

        // With better library support, `currentContext()` should do this
        // for us, which is what happens with HTTP controllers, but with
        // Feign we need to check the MDC ourselves
        final var extraction = mdcExtractor.extract(getMDCAdapter());
        final TraceContext currentContext;
        if (EMPTY == extraction) {
            logger.trace("No trace extraction from MDC");
            currentContext = currentContext(tracer, logger);
        } else {
            logger.trace("Using trace extraction from MDC: {}", extraction);
            currentContext = extraction.context();
        }

        injector.inject(currentContext, template);
    }
}
