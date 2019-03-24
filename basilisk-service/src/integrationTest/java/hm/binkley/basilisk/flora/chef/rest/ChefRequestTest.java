package hm.binkley.basilisk.flora.chef.rest;

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

import static hm.binkley.basilisk.flora.FloraFixtures.CHEF_CODE;
import static hm.binkley.basilisk.flora.FloraFixtures.CHEF_NAME;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import(JsonConfiguration.class)
@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class ChefRequestTest {
    private final JacksonTester<ChefRequest> json;

    @Test
    void shouldComeFromGoodJson()
            throws IOException {
        assertThat(json.readObject("chef-request-test.json"))
                .isEqualTo(ChefRequest.builder()
                        .code(CHEF_CODE)
                        .name(CHEF_NAME)
                        .build());
    }
}
