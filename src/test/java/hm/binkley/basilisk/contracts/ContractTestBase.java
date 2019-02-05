package hm.binkley.basilisk.contracts;

import io.restassured.RestAssured;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@AutoConfigureEmbeddedDatabase
@SpringBootTest(webEnvironment = RANDOM_PORT)
abstract class ContractTestBase {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    void assertEmptyArray(final JSONArray array) {
        assertThat(array).isEmpty();
    }
}
