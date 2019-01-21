package hm.binkley.basilisk;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class BasiliskApplicationTest {
    @Autowired
    private WebTestClient client;

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
}
