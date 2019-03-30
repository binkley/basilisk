package hm.binkley.basilisk.flora.ingredient.rest;

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

import static hm.binkley.basilisk.flora.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.INGREDIENT_CODE;
import static hm.binkley.basilisk.flora.FloraFixtures.INGREDIENT_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.INGREDIENT_QUANTITY;
import static hm.binkley.basilisk.flora.FloraFixtures.RECIPE_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_NAME;
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
                .chefId(CHEF_ID)
                .code(INGREDIENT_CODE)
                .id(INGREDIENT_ID)
                .name(SOURCE_NAME)
                .quantity(INGREDIENT_QUANTITY)
                .recipeId(null)
                .sourceId(SOURCE_ID)
                .build()))
                .isEqualToJson("any-ingredient-unused-response-test.json");
    }

    @Test
    void shouldBecomeGoodJsonWhenUsed()
            throws IOException {
        assertThat(json.write(AnyIngredientResponse.builder()
                .chefId(CHEF_ID)
                .code(INGREDIENT_CODE)
                .id(INGREDIENT_ID)
                .name(SOURCE_NAME)
                .quantity(INGREDIENT_QUANTITY)
                .recipeId(RECIPE_ID)
                .sourceId(SOURCE_ID)
                .build()))
                .isEqualToJson("any-ingredient-used-response-test.json");
    }
}
