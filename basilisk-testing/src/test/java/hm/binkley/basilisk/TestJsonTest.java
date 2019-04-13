package hm.binkley.basilisk;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static hm.binkley.basilisk.TestJson.readTestJson;
import static org.assertj.core.api.Assertions.assertThat;

class TestJsonTest {
    @Test
    void shouldFindTestJson()
            throws IOException {
        assertThat(readTestJson("my-tag")).isNotEmpty();
    }
}
