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
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_NAME;
import static hm.binkley.basilisk.flora.ingredient.rest.UnusedIngredientResponse.using;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@Import(JsonConfiguration.class)
@JsonTest
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UnusedIngredientResponseTest {
    private final JacksonTester<UsedIngredientResponse> json;

    @Test
    void shouldBecomeGoodJson()
            throws IOException {
        assertThat(json.write(UsedIngredientResponse.builder()
                .id(INGREDIENT_ID)
                .sourceId(SOURCE_ID)
                .code(INGREDIENT_CODE)
                .name(SOURCE_NAME)
                .quantity(INGREDIENT_QUANTITY)
                .chefId(CHEF_ID)
                .build()))
                .isEqualToJson("unused-ingredient-response-test.json");
    }

    @Test
    void shouldUse() {
        assertThat(using().from(INGREDIENT_ID, INGREDIENT_CODE, SOURCE_ID,
                SOURCE_NAME, INGREDIENT_QUANTITY, CHEF_ID))
                .isEqualTo(new UnusedIngredientResponse(
                        INGREDIENT_ID, INGREDIENT_CODE, SOURCE_ID,
                        SOURCE_NAME, INGREDIENT_QUANTITY, CHEF_ID));
    }
}