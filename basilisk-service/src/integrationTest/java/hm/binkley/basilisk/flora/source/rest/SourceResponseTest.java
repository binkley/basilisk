package hm.binkley.basilisk.flora.source.rest;

import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.flora.location.rest.LocationResponse;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.Set;

import static hm.binkley.basilisk.flora.FloraFixtures.LOCATION_CODE;
import static hm.binkley.basilisk.flora.FloraFixtures.LOCATION_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.LOCATION_NAME;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_CODE;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import(JsonConfiguration.class)
@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class SourceResponseTest {
    private final JacksonTester<SourceResponse> json;

    @Test
    void shouldBecomeGoodJsonWithNoAvailableAt()
            throws IOException {
        assertThat(json.write(SourceResponse.builder()
                .id(SOURCE_ID)
                .code(SOURCE_CODE)
                .name(SOURCE_NAME)
                .build()))
                .isEqualToJson(
                        "source-with-no-available-at-response-test.json");
    }

    @Test
    void shouldBecomeGoodJsonWithSomeAvailableAt()
            throws IOException {
        assertThat(json.write(SourceResponse.builder()
                .id(SOURCE_ID)
                .code(SOURCE_CODE)
                .name(SOURCE_NAME)
                .availableAt(Set.of(LocationResponse.builder()
                        .id(LOCATION_ID)
                        .code(LOCATION_CODE)
                        .name(LOCATION_NAME)
                        .build()))
                .build()))
                .isEqualToJson(
                        "source-with-some-available-at-response-test.json");
    }
}
