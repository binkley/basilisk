package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.configuration.JsonConfiguration;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.SOURCE_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.SOURCE_NAME;
import static hm.binkley.basilisk.flora.rest.SourceResponse.with;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import(JsonConfiguration.class)
@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class SourceResponseTest {
    private final JacksonTester<SourceResponse> json;

    @Test
    void shouldBecomeGoodJson()
            throws IOException {
        assertThat(json.write(SourceResponse.builder()
                .id(SOURCE_ID)
                .name(SOURCE_NAME)
                .build()))
                .isEqualToJson("source-response-test.json");
    }

    @Test
    void shouldUse() {
        assertThat(with().from(SOURCE_ID, SOURCE_NAME))
                .isEqualTo(new SourceResponse(
                        SOURCE_ID, SOURCE_NAME));
    }
}
