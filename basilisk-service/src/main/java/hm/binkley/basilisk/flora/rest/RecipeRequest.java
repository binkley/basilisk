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
    private final Set<UsedIngredientRequest> ingredients
            = new LinkedHashSet<>();

    public <R, I> R as(final RecipeRequest.As<R, I> asRecipe,
            final UsedIngredientRequest.As<I> asUsedIngredient) {
        return asRecipe.from(name, ingredients.stream()
                .map(it -> asUsedIngredient.from(it.getName())));
    }

    public interface As<R, I> {
        R from(final String name, final Stream<I> ingredients);
    }
}
