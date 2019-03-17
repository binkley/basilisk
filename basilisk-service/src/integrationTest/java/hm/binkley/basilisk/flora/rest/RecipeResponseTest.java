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

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.INGREDIENT_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.INGREDIENT_QUANTITY;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.SOURCE_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.SOURCE_NAME;
import static hm.binkley.basilisk.flora.rest.RecipeResponse.with;
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
                .chefId(CHEF_ID)
                .build()))
                .isEqualToJson(
                        "recipe-with-no-ingredients-response-test.json");
    }

    @Test
    void shouldBecomeGoodJsonWithSomeIngredients()
            throws IOException {
        final var recipeId = 33L;

        assertThat(json.write(RecipeResponse.builder()
                .id(recipeId)
                .name("SOUFFLE")
                .chefId(CHEF_ID)
                .ingredients(Set.of(UsedIngredientResponse.builder()
                        .id(INGREDIENT_ID)
                        .sourceId(SOURCE_ID)
                        .name(SOURCE_NAME)
                        .quantity(INGREDIENT_QUANTITY)
                        .recipeId(recipeId)
                        .chefId(CHEF_ID)
                        .build()))
                .build()))
                .isEqualToJson(
                        "recipe-with-some-ingredients-response-test.json");
    }

    @Test
    void shouldUse() {
        final var recipeId = 33L;
        final var name = "SOUFFLE";
        final var ingredientResponse = new UsedIngredientResponse(
                INGREDIENT_ID, SOURCE_ID, SOURCE_NAME,
                INGREDIENT_QUANTITY, recipeId, CHEF_ID);

        assertThat(with(true).from(
                recipeId, name, CHEF_ID, Stream.of(ingredientResponse)))
                .isEqualTo(new RecipeResponse(recipeId, name, true, CHEF_ID,
                        Set.of(ingredientResponse)));
    }
}
