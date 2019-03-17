package hm.binkley.basilisk.flora.rest;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.SOURCE_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.SOURCE_NAME;
import static org.assertj.core.api.Assertions.assertThat;

class UnusedIngredientRequestTest {
    @Test
    void shouldConvert() {
        final var request = UnusedIngredientRequest.builder()
                .sourceId(SOURCE_ID)
                .name(SOURCE_NAME)
                .chefId(CHEF_ID)
                .build();

        assertThat(request.as(Ingredienty::new))
                .isEqualTo(new Ingredienty(request.getSourceId(),
                        request.getName(), request.getQuantity(),
                        request.getChefId()));
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    @ToString
    private static final class Ingredienty {
        private final Long sourceId;
        private final String name;
        private final BigDecimal quantity;
        private final Long chefId;
    }
}
