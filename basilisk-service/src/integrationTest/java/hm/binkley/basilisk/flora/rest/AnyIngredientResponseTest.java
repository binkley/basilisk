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

import static hm.binkley.basilisk.flora.rest.UsedIngredientResponse.with;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import(JsonConfiguration.class)
@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class AnyIngredientResponseTest {
    private final JacksonTester<AnyIngredientResponse> json;

    @Test
    void shouldBecomeGoodJsonWhenUnused()
            throws IOException {
        assertThat(json.write(AnyIngredientResponse.builder()
                .id(31L)
                .name("EGGS")
                .recipeId(null)
                .chefId(17L)
                .build()))
                .isEqualToJson("any-ingredient-unused-response-test.json");
    }

    @Test
    void shouldBecomeGoodJsonWhenUsed()
            throws IOException {
        assertThat(json.write(AnyIngredientResponse.builder()
                .id(31L)
                .name("EGGS")
                .recipeId(2L)
                .chefId(17L)
                .build()))
                .isEqualToJson("any-ingredient-used-response-test.json");
    }

    @Test
    void shouldUse() {
        final var id = 31L;
        final var name = "EGGS";
        final var recipeId = 2L;
        final var chefId = 17L;

        assertThat(with().from(id, name, recipeId, chefId))
                .isEqualTo(new UsedIngredientResponse(
                        id, name, recipeId, chefId));
    }
}
