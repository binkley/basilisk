package hm.binkley.basilisk;

import feign.Request;
import feign.Response;
import lombok.Generated;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpMessage;
import org.apache.http.HttpResponse;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.message.BasicHttpEntityEnclosingRequest;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.httpclient.LogbookHttpRequestInterceptor;
import org.zalando.logbook.httpclient.LogbookHttpResponseInterceptor;

import java.io.IOError;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import static feign.Util.toByteArray;
import static org.apache.http.HttpVersion.HTTP_1_1;

@Component
@Generated // Lie to JaCoCo -- TODO: Incomplete testing of corner cases
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class LogbookFeignLogger
        extends feign.Logger {
    private final ThreadLocal<HttpContext> context
            = new ThreadLocal<>();

    private final Logbook logbook;
    private final Logger logger;

    @SuppressWarnings("PMD.DataflowAnomalyAnalysis")
    private static void copyHeadersTo(
            final Map<String, Collection<String>> headers,
            final HttpMessage message) {
        // If "values" is null, consider that a pathological case
        headers.forEach((header, values) ->
                values.forEach(value ->
                        message.addHeader(header, value)));
    }

    private static void copyBodyTo(final byte[] bodyData,
            final HttpEntityEnclosingRequest logbookRequest) {
        logbookRequest.setEntity(new ByteArrayEntity(
                null == bodyData ? new byte[0] : bodyData));
    }

    private static void copyBodyTo(final byte[] bodyData,
            final HttpResponse logbookResponse) {
        logbookResponse.setEntity(new ByteArrayEntity(bodyData));
    }

    private static byte[] bodyData(final Request request) {
        final var requestBody = request.requestBody();
        return null == requestBody
                ? new byte[0] : requestBody.asBytes();
    }

    private static byte[] bodyData(final Response response)
            throws IOException {
        final var status = response.status();
        if (response.body() != null
                && !(status == 204 || status == 205)) {
            return toByteArray(response.body().asInputStream());
        } else {
            return new byte[0];
        }
    }

    private static HttpEntityEnclosingRequest logbookRequestFor(
            final Request feignRequest) {
        final var logbookRequest
                = new BasicHttpEntityEnclosingRequest(
                feignRequest.httpMethod().name(),
                feignRequest.url(),
                HTTP_1_1);

        copyHeadersTo(feignRequest.headers(), logbookRequest);
        copyBodyTo(bodyData(feignRequest), logbookRequest);

        return logbookRequest;
    }

    private static HttpResponse logbookResponseFor(
            final Response feignResponse,
            final byte[] bodyData) {
        final var logbookResponse = new BasicHttpResponse(HTTP_1_1,
                feignResponse.status(), feignResponse.reason());

        copyHeadersTo(feignResponse.headers(), logbookResponse);
        copyBodyTo(bodyData, logbookResponse);

        return logbookResponse;
    }

    @Override
    protected void log(final String configKey,
            final String format, final Object... args) {
        // Do nothing -- Feign logger is unfortunate
    }

    @Override
    protected void logRetry(final String configKey,
            final Level logLevel) {
        logger.warn("Retrying {}", configKey);
    }

    @Override
    protected IOException logIOException(final String configKey,
            final Level logLevel, final IOException ioe,
            final long elapsedTime) {
        context.remove();
        logger.error("Failed {} after {} ms: {}", configKey,
                elapsedTime, ioe.toString(), ioe);
        return ioe;
    }

    @Override
    protected void logRequest(final String configKey,
            final Level logLevel, final Request feignRequest) {
        try {
            final HttpEntityEnclosingRequest logbookRequest
                    = logbookRequestFor(feignRequest);

            final var context = new HttpClientContext();
            new LogbookHttpRequestInterceptor(logbook).process(
                    logbookRequest, context);
            this.context.set(context);
        } catch (final HttpException | IOException e) {
            // It is unfortunate the logbook request interceptor throws
            throw new IOError(e);
        }
    }

    @Override
    protected Response logAndRebufferResponse(final String configKey,
            final Level logLevel,
            final Response feignResponse,
            final long elapsedTime)
            throws IOException {
        final byte[] bodyData = bodyData(feignResponse);
        final HttpResponse logbookResponse = logbookResponseFor(
                feignResponse, bodyData);

        new LogbookHttpResponseInterceptor().process(
                logbookResponse, context.get());
        context.remove();

        return feignResponse.toBuilder().body(bodyData).build();
    }
}
