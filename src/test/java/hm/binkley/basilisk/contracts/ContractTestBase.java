package hm.binkley.basilisk.contracts;

import io.restassured.module.mockmvc.RestAssuredMockMvc;
import io.zonky.test.db.AutoConfigureEmbeddedDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.web.context.WebApplicationContext;

@AutoConfigureEmbeddedDatabase
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class ContractTestBase {
    @Autowired
    protected WebApplicationContext context;

    @BeforeEach
    void setUp() {
        RestAssuredMockMvc.webAppContextSetup(context);
    }
}
