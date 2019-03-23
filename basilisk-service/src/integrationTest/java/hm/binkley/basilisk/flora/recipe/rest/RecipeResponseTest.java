package hm.binkley.basilisk.flora.recipe.rest;

import hm.binkley.basilisk.configuration.JsonConfiguration;
import hm.binkley.basilisk.flora.ingredient.rest.UsedIngredientResponse;
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

import static hm.binkley.basilisk.flora.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.INGREDIENT_CODE;
import static hm.binkley.basilisk.flora.FloraFixtures.INGREDIENT_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.INGREDIENT_QUANTITY;
import static hm.binkley.basilisk.flora.FloraFixtures.RECIPE_CODE;
import static hm.binkley.basilisk.flora.FloraFixtures.RECIPE_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.RECIPE_NAME;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_NAME;
import static hm.binkley.basilisk.flora.recipe.rest.RecipeResponse.using;
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
                .id(RECIPE_ID)
                .code(RECIPE_CODE)
                .name(RECIPE_NAME)
                .chefId(CHEF_ID)
                .build()))
                .isEqualToJson(
                        "recipe-with-no-ingredients-response-test.json");
    }

    @Test
    void shouldBecomeGoodJsonWithSomeIngredients()
            throws IOException {
        assertThat(json.write(RecipeResponse.builder()
                .id(RECIPE_ID)
                .code(RECIPE_CODE)
                .name(RECIPE_NAME)
                .chefId(CHEF_ID)
                .ingredients(Set.of(UsedIngredientResponse.builder()
                        .id(INGREDIENT_ID)
                        .code(INGREDIENT_CODE)
                        .sourceId(SOURCE_ID)
                        .name(SOURCE_NAME)
                        .quantity(INGREDIENT_QUANTITY)
                        .recipeId(RECIPE_ID)
                        .chefId(CHEF_ID)
                        .build()))
                .build()))
                .isEqualToJson(
                        "recipe-with-some-ingredients-response-test.json");
    }

    @Test
    void shouldUse() {
        final var ingredientResponse = new UsedIngredientResponse(
                INGREDIENT_ID, INGREDIENT_CODE, SOURCE_ID, SOURCE_NAME,
                INGREDIENT_QUANTITY, RECIPE_ID, CHEF_ID);

        assertThat(using(true).from(
                RECIPE_ID, RECIPE_CODE, RECIPE_NAME, CHEF_ID,
                Stream.of(ingredientResponse)))
                .isEqualTo(new RecipeResponse(RECIPE_ID, RECIPE_CODE,
                        RECIPE_NAME, true, CHEF_ID,
                        Set.of(ingredientResponse)));
    }
}
