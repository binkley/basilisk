package hm.binkley.basilisk;

import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

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
    void shouldFindRestApi() {
        client.get().uri("/basilisk")
                .exchange()
                .expectStatus().isOk();
    }

    @Test
    void shouldFindAdminHomePage() {
        client.get().uri("/admin")
                .exchange()
                .expectStatus().isOk();
    }
}
