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

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.INGREDIENT_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.INGREDIENT_NAME;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.INGREDIENT_QUANTITY;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.RECIPE_ID;
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
                .name(INGREDIENT_NAME)
                .quantity(INGREDIENT_QUANTITY)
                .recipeId(null)
                .chefId(CHEF_ID)
                .build()))
                .isEqualToJson("any-ingredient-unused-response-test.json");
    }

    @Test
    void shouldBecomeGoodJsonWhenUsed()
            throws IOException {
        assertThat(json.write(AnyIngredientResponse.builder()
                .id(INGREDIENT_ID)
                .name(INGREDIENT_NAME)
                .quantity(INGREDIENT_QUANTITY)
                .recipeId(RECIPE_ID)
                .chefId(CHEF_ID)
                .build()))
                .isEqualToJson("any-ingredient-used-response-test.json");
    }

    @Test
    void shouldUse() {
        assertThat(with().from(INGREDIENT_ID, INGREDIENT_NAME,
                INGREDIENT_QUANTITY, RECIPE_ID, CHEF_ID))
                .isEqualTo(new UsedIngredientResponse(
                        INGREDIENT_ID, INGREDIENT_NAME, INGREDIENT_QUANTITY,
                        RECIPE_ID, CHEF_ID));
    }
}
