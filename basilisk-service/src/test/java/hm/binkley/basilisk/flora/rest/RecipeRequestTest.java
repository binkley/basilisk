package hm.binkley.basilisk.flora.rest;

import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;
import static org.assertj.core.api.Assertions.assertThat;

class RecipeRequestTest {
    @Test
    void shouldConvert() {
        final var ingredientRequest = UsedIngredientRequest.builder()
                .name("EGGS")
                .build();
        final var request = RecipeRequest.builder()
                .name("SOUFFLE")
                .ingredients(Set.of(ingredientRequest))
                .build();

        assertThat(request.as(Recipey::from, Ingredientey::new))
                .isEqualTo(new Recipey(request.getName(), Set.of(
                        new Ingredientey(ingredientRequest.getName()))));
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    @ToString
    private static final class Recipey {
        private final String name;
        private final Set<Ingredientey> ingredients;

        private static Recipey from(final String name,
                final Stream<Ingredientey> ingredients) {
            return new Recipey(name,
                    ingredients.collect(toCollection(LinkedHashSet::new)));
        }
    }

    @EqualsAndHashCode
    @RequiredArgsConstructor
    @ToString
    private static final class Ingredientey {
        private final String name;
    }
}
