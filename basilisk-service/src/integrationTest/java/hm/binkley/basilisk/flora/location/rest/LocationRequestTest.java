package hm.binkley.basilisk.flora.location.rest;

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

import static hm.binkley.basilisk.flora.FloraFixtures.LOCATION_CODE;
import static hm.binkley.basilisk.flora.FloraFixtures.LOCATION_NAME;
import static org.assertj.core.api.Java6Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import(JsonConfiguration.class)
@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class LocationRequestTest {
    private final JacksonTester<LocationRequest> json;

    @Test
    void shouldComeFromGoodJson()
            throws IOException {
        assertThat(json.readObject("location-request-test.json"))
                .isEqualTo(LocationRequest.builder()
                        .code(LOCATION_CODE)
                        .name(LOCATION_NAME)
                        .build());
    }
}
