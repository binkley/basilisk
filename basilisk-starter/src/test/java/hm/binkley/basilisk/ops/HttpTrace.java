package hm.binkley.basilisk.ops;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import hm.binkley.basilisk.Bug;
import hm.binkley.basilisk.ops.HttpTrace.RequestTrace;
import hm.binkley.basilisk.ops.HttpTrace.ResponseTrace;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.Value;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.slf4j.Logger;

import java.io.IOError;
import java.io.IOException;
import java.net.URI;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Stream;

@Data
@EqualsAndHashCode(exclude = "correlation")
@JsonTypeInfo(use = Id.NAME, property = "type")
@JsonSubTypes({
        @JsonSubTypes.Type(RequestTrace.class),
        @JsonSubTypes.Type(ResponseTrace.class)
})
public class HttpTrace {
    protected String origin;
    protected String type;
    protected String correlation;
    protected String protocol;
    protected Map<String, List<String>> headers;
    protected JsonNode body;

    public static Stream<String> httpHeaderTracesOf(
            final Logger httpLogger, final ObjectMapper objectMapper,
            final String headerName) {
        return httpTracesOf(httpLogger, objectMapper)
                .map(HttpTrace::getHeaders)
                .map(Map::entrySet)
                .flatMap(Set::stream)
                .filter(header -> header.getKey()
                        .equalsIgnoreCase(headerName))
                .map(Entry::getValue)
                .flatMap(Collection::stream);
    }

    public static Stream<HttpTrace> httpTracesOf(
            final Logger httpLogger, final ObjectMapper objectMapper) {
        final var captured = ArgumentCaptor.forClass(String.class);
        Mockito.verify(httpLogger, Mockito.atLeast(1))
                .trace(captured.capture());

        return captured.getAllValues().stream()
                .map(message -> httpTraceOf(message, objectMapper));
    }

    private static HttpTrace httpTraceOf(final String logMessage,
            final ObjectMapper objectMapper) {
        try {
            return objectMapper.readValue(logMessage, HttpTrace.class);
        } catch (final IOException e) {
            throw new IOError(e);
        }
    }

    public boolean isProblem() {
        return headers.containsValue(List.of("application/problem+json"));
    }

    public <T> T getBodyAs(
            final Class<T> type, final ObjectMapper objectMapper) {
        try {
            return objectMapper.treeToValue(body, type);
        } catch (final JsonProcessingException e) {
            throw new Bug("Not a " + type.getName() + ": " + body + ": " + e,
                    e);
        }
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    @JsonTypeName("request")
    @ToString(callSuper = true)
    public static class RequestTrace
            extends HttpTrace {
        String remote;
        String method;
        URI uri;
    }

    @Value
    @EqualsAndHashCode(callSuper = true)
    @JsonTypeName("response")
    @ToString(callSuper = true)
    public static class ResponseTrace
            extends HttpTrace {
        int duration;
        int status;
    }
}
