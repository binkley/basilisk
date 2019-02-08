package hm.binkley.basilisk.rest;

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
import java.time.ZonedDateTime;

import static java.time.ZoneOffset.UTC;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import(JsonConfiguration.class)
@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class BasiliskResponseTest {
    private final JacksonTester<BasiliskResponse> json;

    @Test
    void shouldBecomeGoodJson()
            throws IOException {
        assertThat(json.write(BasiliskResponse.builder()
                .id(31L)
                .word("BOB")
                .at(ZonedDateTime.of(
                        2011, 2, 3, 4, 5, 6, 7_000_000, UTC.normalized())
                        .toInstant())
                .extra("BONUS")
                .build()))
                .isEqualToJson("basilisk-response-test.json");
    }
}
