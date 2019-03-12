package hm.binkley.basilisk.flora.rest;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Stream;

@Builder
@Data
@RequiredArgsConstructor
public final class RecipeRequest {
    private final @Length(min = 3, max = 32) String name;
    private final @NotNull Long chefId;
    @Builder.Default
    private final Set<UsedIngredientRequest> ingredients
            = new LinkedHashSet<>();

    public <R, I> R as(final RecipeRequest.As<R, I> asRecipe,
            final UsedIngredientRequest.As<I> asUsedIngredient) {
        return asRecipe.from(getName(), getChefId(), getIngredients().stream()
                .map(it -> asUsedIngredient.from(
                        it.getName(), it.getChefId())));
    }

    public interface As<R, I> {
        R from(final String name, final Long chefId,
                final Stream<I> ingredients);
    }
}
