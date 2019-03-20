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

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.CHEF_CODE;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.CHEF_NAME;
import static hm.binkley.basilisk.flora.rest.ChefResponse.using;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import(JsonConfiguration.class)
@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ChefResponseTest {
    private final JacksonTester<ChefResponse> json;

    @Test
    void shouldBecomeGoodJson()
            throws IOException {
        assertThat(json.write(ChefResponse.builder()
                .id(CHEF_ID)
                .code(CHEF_CODE)
                .name(CHEF_NAME)
                .build()))
                .isEqualToJson("chef-response-test.json");
    }

    @Test
    void shouldUse() {
        assertThat(using().from(CHEF_ID, CHEF_CODE, CHEF_NAME))
                .isEqualTo(new ChefResponse(CHEF_ID, CHEF_CODE, CHEF_NAME));
    }
}
