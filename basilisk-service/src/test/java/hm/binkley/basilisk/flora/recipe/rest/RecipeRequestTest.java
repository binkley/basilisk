package hm.binkley.basilisk.flora.recipe.rest;

import hm.binkley.basilisk.flora.ingredient.rest.UsedIngredientRequest;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Stream;

import static hm.binkley.basilisk.flora.FloraFixtures.CHEF_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.INGREDIENT_CODE;
import static hm.binkley.basilisk.flora.FloraFixtures.INGREDIENT_QUANTITY;
import static hm.binkley.basilisk.flora.FloraFixtures.RECIPE_CODE;
import static hm.binkley.basilisk.flora.FloraFixtures.RECIPE_NAME;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_ID;
import static hm.binkley.basilisk.flora.FloraFixtures.SOURCE_NAME;
import static java.util.stream.Collectors.toCollection;
import static org.assertj.core.api.Assertions.assertThat;

class RecipeRequestTest {
    @Test
    void shouldConvert() {
        final var ingredientRequest = UsedIngredientRequest.builder()
                .code(INGREDIENT_CODE)
                .sourceId(SOURCE_ID)
                .name(SOURCE_NAME)
                .quantity(INGREDIENT_QUANTITY)
                .chefId(CHEF_ID)
                .build();
        final var request = RecipeRequest.builder()
                .code(RECIPE_CODE)
                .name(RECIPE_NAME)
                .ingredients(new TreeSet<>(Set.of(ingredientRequest)))
                .build();

        assertThat(request.as(Recipey::from, Ingredientey::new))
                .isEqualTo(new Recipey(request.getCode(), request.getName(),
                        request.getChefId(),
                        new TreeSet<>(Set.of(new Ingredientey(
                                ingredientRequest.getCode(),
                                ingredientRequest.getSourceId(),
                                ingredientRequest.getName(),
                                ingredientRequest.getQuantity(),
                                ingredientRequest.getChefId())))));
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    @ToString
    private static final class Recipey {
        private final String code;
        private final String name;
        private final Long chefId;
        private final SortedSet<Ingredientey> ingredients;

        private static Recipey from(final String code, final String name,
                final Long chefId, final Stream<Ingredientey> ingredients) {
            return new Recipey(code, name, chefId,
                    ingredients.collect(toCollection(TreeSet::new)));
        }
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    @ToString
    private static final class Ingredientey
            implements Comparable<Ingredientey> {
        private final String code;
        private final Long sourceId;
        private final String name;
        private final BigDecimal quantity;
        private final Long chefId;

        @Override
        public int compareTo(final Ingredientey that) {
            return code.compareTo(that.code);
        }
    }
}
