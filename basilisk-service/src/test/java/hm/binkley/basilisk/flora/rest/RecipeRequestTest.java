package hm.binkley.basilisk.flora.rest;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.SOURCE_NAME;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.INGREDIENT_QUANTITY;
import static hm.binkley.basilisk.flora.domain.store.FloraFixtures.SOURCE_ID;
import static java.util.stream.Collectors.toCollection;
import static org.assertj.core.api.Assertions.assertThat;

class RecipeRequestTest {
    @Test
    void shouldConvert() {
        final var ingredientRequest = UsedIngredientRequest.builder()
                .sourceId(SOURCE_ID)
                .name(SOURCE_NAME)
                .quantity(INGREDIENT_QUANTITY)
                .chefId(CHEF_ID)
                .build();
        final var request = RecipeRequest.builder()
                .name("SOUFFLE")
                .ingredients(Set.of(ingredientRequest))
                .build();

        assertThat(request.as(Recipey::from, Ingredientey::new))
                .isEqualTo(new Recipey(request.getName(), request.getChefId(),
                        Set.of(new Ingredientey(
                                ingredientRequest.getSourceId(),
                                ingredientRequest.getName(),
                                ingredientRequest.getQuantity(),
                                ingredientRequest.getChefId()))));
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    @ToString
    private static final class Recipey {
        private final String name;
        private final Long chefId;
        private final Set<Ingredientey> ingredients;

        private static Recipey from(final String name, final Long chefId,
                final Stream<Ingredientey> ingredients) {
            return new Recipey(name, chefId,
                    ingredients.collect(toCollection(LinkedHashSet::new)));
        }
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    @ToString
    private static final class Ingredientey {
        private final Long sourceId;
        private final String name;
        private final BigDecimal quantity;
        private final Long chefId;
    }
}
