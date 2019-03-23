package hm.binkley.basilisk.flora.ingredient.rest;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static hm.binkley.basilisk.flora.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.INGREDIENT_CODE;
import static hm.binkley.basilisk.flora.FloraFixtures.INGREDIENT_QUANTITY;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_NAME;
import static org.assertj.core.api.Assertions.assertThat;

class UsedIngredientRequestTest {
    @Test
    void shouldConvert() {
        final var request = UsedIngredientRequest.builder()
                .code(INGREDIENT_CODE)
                .sourceId(SOURCE_ID)
                .name(SOURCE_NAME)
                .quantity(INGREDIENT_QUANTITY)
                .chefId(CHEF_ID)
                .build();

        assertThat(request.as(Ingredienty::new)).isEqualTo(new Ingredienty(
                request.getCode(),
                request.getSourceId(),
                request.getName(),
                request.getQuantity(),
                request.getChefId()));
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    @ToString
    private static final class Ingredienty {
        private final String code;
        private final Long sourceId;
        private final String name;
        private final BigDecimal quantity;
        private final Long chefId;
    }
}
