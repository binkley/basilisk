package hm.binkley.basilisk.flora.source.rest;

import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.flora.location.rest.LocationRequest;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_CODE;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_NAME;
import static org.assertj.core.api.Java6Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import(JsonConfiguration.class)
@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class SourceRequestTest {
    private final JacksonTester<SourceRequest> json;

    @Test
    void shouldComeFromGoodJsonWithNoAvailableAt()
            throws IOException {
        assertThat(json.readObject(
                "source-with-no-available-at-request-test.json"))
                .isEqualTo(SourceRequest.builder()
                        .code(SOURCE_CODE)
                        .name(SOURCE_NAME)
                        .build());
    }

    @Test
    void shouldComeFromGoodJsonWithSomeAvailableAt()
            throws IOException {
        assertThat(json.readObject(
                "source-with-some-available-at-request-test.json"))
                .isEqualTo(SourceRequest.builder()
                        .code(SOURCE_CODE)
                        .name(SOURCE_NAME)
                        .availableAt(List.of(LocationRequest.builder()
                                .code("DAL")
                                .name("The Dallas Yellow Rose")
                                .build()))
                        .build());
    }
}
