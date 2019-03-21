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

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.LOCATION_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.LOCATION_NAME;
import static hm.binkley.basilisk.flora.rest.LocationResponse.using;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import(JsonConfiguration.class)
@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class LocationResponseTest {
    private final JacksonTester<LocationResponse> json;

    @Test
    void shouldBecomeGoodJson()
            throws IOException {
        assertThat(json.write(LocationResponse.builder()
                .id(LOCATION_ID)
                .name(LOCATION_NAME)
                .build()))
                .isEqualToJson("location-response-test.json");
    }

    @Test
    void shouldUse() {
        assertThat(using().from(LOCATION_ID, LOCATION_NAME))
                .isEqualTo(new LocationResponse(LOCATION_ID, LOCATION_NAME));
    }
}
