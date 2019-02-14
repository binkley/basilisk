package hm.binkley.basilisk.contracts;

import hm.binkley.basilisk.BasiliskApplication;
import io.restassured.RestAssured;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@AutoConfigureEmbeddedDatabase
@ContextConfiguration(classes = BasiliskApplication.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
class ContractTestBase {
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
