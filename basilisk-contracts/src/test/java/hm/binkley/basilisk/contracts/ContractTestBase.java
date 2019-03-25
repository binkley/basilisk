package hm.binkley.basilisk.contracts;

import hm.binkley.basilisk.BasiliskApplication;
import io.restassured.RestAssured;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import net.minidev.json.JSONArray;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ContextConfiguration;

import static io.restassured.filter.log.LogDetail.ALL;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@AutoConfigureEmbeddedDatabase
@ContextConfiguration(classes = BasiliskApplication.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@TestMethodOrder(Alphanumeric.class)
abstract class ContractTestBase {
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
        RestAssured.config.getLogConfig()
                .enablePrettyPrinting(true)
                .enableLoggingOfRequestAndResponseIfValidationFails(ALL);
    }

    void assertEmptyArray(final JSONArray array) {
        assertThat(array).isEmpty();
    }
}
