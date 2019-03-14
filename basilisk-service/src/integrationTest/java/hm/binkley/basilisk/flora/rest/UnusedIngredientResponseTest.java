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
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.INGREDIENT_NAME;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.INGREDIENT_QUANTITY;
import static hm.binkley.basilisk.flora.rest.UnusedIngredientResponse.using;
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
                .id(31L)
                .name(INGREDIENT_NAME)
                .quantity(INGREDIENT_QUANTITY)
                .chefId(CHEF_ID)
                .build()))
                .isEqualToJson("unused-ingredient-response-test.json");
    }

    @Test
    void shouldUse() {
        final var id = 31L;
        final var name = INGREDIENT_NAME;
        final var quantity = INGREDIENT_QUANTITY;

        assertThat(using().from(id, name, quantity, CHEF_ID))
                .isEqualTo(new UnusedIngredientResponse(
                        id, name, quantity, CHEF_ID));
    }
}
