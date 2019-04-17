package hm.binkley.basilisk;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpHeaders.IF_NONE_MATCH;

@AutoConfigureEmbeddedDatabase
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SpringBootTest(webEnvironment = RANDOM_PORT)
@SuppressWarnings("PMD.AvoidDuplicateLiterals")
@TestInstance(PER_CLASS)
class BasiliskLiveTest {
    private final WebTestClient client;

    @LocalServerPort
    private int port;

    @Test
    void shouldFindApplicationHomePage() {
        client.get().uri("/index.html")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldFindActuatorHealth() {
        client.get().uri("/actuator/health")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldFindSwaggerRestApi() {
        client.get().uri("/v2/api-docs")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldFindSwaggerHomePage() {
        client.get().uri("/swagger-ui.html")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldFindDataRestHomePage() {
        client.get().uri("/data/browser/index.html")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldFindAdminHomePage() {
        client.get().uri("/admin")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldUseEtags() {
        final var etag = new AtomicReference<String>();
        client.get().uri("/chefs")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().value(ETAG, etag::set);
        client.get().uri("/chefs")
                .header(IF_NONE_MATCH, etag.get())
                .exchange()
                .expectStatus().isNotModified();
    }

    @Test
    void shouldProvideTracingHeadersWhenNoneProffered() {
        client.get().uri("/chefs")
                .exchange()
                .expectHeader().value("X-B3-TraceId", notNullValue());
    }

    @Disabled("TODO: Works from the command line")
    @Test
    void shouldPreserveTracingHeaders() {
        final var traceId = String.format("%016X", 1234);

        client.get().uri("/chefs")
                .header("X-B3-TraceId", traceId)
                .header("X-B3-SpanId", traceId)
                .header("X-B3-ParentSpanId", traceId)
                .exchange()
                .expectHeader().value("X-B3-TraceId", equalTo(traceId))
                .expectHeader().value("X-B3-SpanId", notNullValue())
                .expectHeader()
                .value("X-B3-ParentSpanId", notNullValue());
    }
}
