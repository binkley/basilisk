package hm.binkley.basilisk;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static hm.binkley.basilisk.TestJson.readTestJson;
import static hm.binkley.basilisk.TestJson.readTestJsonRequest;
import static hm.binkley.basilisk.TestJson.readTestJsonResponse;
import static org.assertj.core.api.Assertions.assertThat;

class TestJsonTest {
    @Test
    void shouldFindTestJson()
            throws IOException {
        assertThat(readTestJson("my-tag")).isNotEmpty();
    }

    @Test
    void shouldFindTestJsonRequest()
            throws IOException {
        assertThat(readTestJsonRequest()).isNotEmpty();
    }

    @Test
    void shouldFindTestJsonResponse()
            throws IOException {
        assertThat(readTestJsonResponse()).isNotEmpty();
    }
}
