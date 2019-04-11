package hm.binkley.basilisk;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpHeaders.ETAG;
import static org.springframework.http.HttpHeaders.IF_NONE_MATCH;

@AutoConfigureEmbeddedDatabase
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestInstance(PER_CLASS)
class BasiliskLiveTest {
    private final WebTestClient client;

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
    void shouldFindRestApi() {
        client.get().uri("/chefs")
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
}
