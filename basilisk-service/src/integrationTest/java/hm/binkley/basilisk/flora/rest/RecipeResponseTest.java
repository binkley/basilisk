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

import static hm.binkley.basilisk.flora.rest.RecipeResponse.using;
import static java.time.Instant.EPOCH;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import(JsonConfiguration.class)
@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class RecipeResponseTest {
    private final JacksonTester<RecipeResponse> json;

    @Test
    void shouldBecomeGoodJsonWithNoIngredients()
            throws IOException {
        assertThat(json.write(RecipeResponse.builder()
                .id(33L)
                .name("SOUFFLE")
                .build()))
                .isEqualToJson(
                        "recipe-with-no-ingredients-response-test.json");
    }

    @Test
    void shouldBecomeGoodJsonWithSomeIngredients()
            throws IOException {
        assertThat(json.write(RecipeResponse.builder()
                .id(33L)
                .name("SOUFFLE")
                .ingredients(Set.of(IngredientResponse.builder()
                        .id(31L)
                        .name("EGGS")
                        .build()))
                .build()))
                .isEqualToJson(
                        "recipe-with-some-ingredients-response-test.json");
    }

    @Test
    void shouldUse() {
        final var id = 33L;
        final var name = "SOUFFLE";
        final var ingredientResponse = new IngredientResponse(31L, "EGGS");

        assertThat(using().from(id, EPOCH, name,
                Stream.of(ingredientResponse)))
                .isEqualTo(new RecipeResponse(id, name,
                        Set.of(ingredientResponse)));
    }
}