package hm.binkley.basilisk.flora.rest;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.INGREDIENT_NAME;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.INGREDIENT_QUANTITY;
import static org.assertj.core.api.Assertions.assertThat;

class UsedIngredientRequestTest {
    @Test
    void shouldConvert() {
        final var request = UsedIngredientRequest.builder()
                .name(INGREDIENT_NAME)
                .quantity(INGREDIENT_QUANTITY)
                .chefId(CHEF_ID)
                .build();

        assertThat(request.as(Ingredienty::new))
                .isEqualTo(new Ingredienty(request.getName(),
                        request.getQuantity(), request.getChefId()));
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    @ToString
    private static final class Ingredienty {
        private final String name;
        private final BigDecimal quantity;
        private final Long chefId;
    }
}
