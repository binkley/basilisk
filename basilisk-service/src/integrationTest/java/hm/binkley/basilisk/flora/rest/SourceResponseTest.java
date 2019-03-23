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
import java.util.Set;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.LOCATION_CODE;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.LOCATION_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.LOCATION_NAME;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.SOURCE_CODE;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.SOURCE_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.SOURCE_NAME;
import static hm.binkley.basilisk.flora.rest.SourceResponse.using;
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
                        .name(LOCATION_NAME)
                        .build()))
                .build()))
                .isEqualToJson(
                        "source-with-some-available-at-response-test.json");
    }

    @Test
    void shouldUse() {
        final var locationResponse = new LocationResponse(
                LOCATION_ID, LOCATION_CODE, LOCATION_NAME);
        assertThat(using().from(SOURCE_ID, SOURCE_CODE, SOURCE_NAME,
                Stream.of(locationResponse)))
                .isEqualTo(new SourceResponse(SOURCE_ID, SOURCE_CODE,
                        SOURCE_NAME, Set.of(locationResponse)));
    }
}
