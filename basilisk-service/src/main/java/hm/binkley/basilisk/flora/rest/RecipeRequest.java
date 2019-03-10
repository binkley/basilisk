package hm.binkley.basilisk.flora.rest;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

@Builder
@Data
@RequiredArgsConstructor
public final class RecipeRequest {
    private final @Length(min = 3, max = 32) String name;
    @Builder.Default
    private final Set<IngredientRequest> ingredients = new LinkedHashSet<>();

    public <T, U> T as(final RecipeRequest.As<T, U> asRecipe,
            final IngredientRequest.As<U> asIngredient) {
        return asRecipe.from(name, ingredients.stream()
                .map(it -> asIngredient.from(it.getName())));
    }

    public interface As<T, U> {
        T from(final String name, final Stream<U> ingredients);
    }
}
