package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.RecipeRecord;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.stream.Stream;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public final class Recipe {
    private final RecipeRecord record;

    public Stream<UsedIngredient> ingredients() {
        return record.getIngredients().stream()
                .map(UsedIngredient::new);
    }

    public <R, I> R as(final Recipe.As<R, I> asRecipe,
            final UsedIngredient.As<I> asUsedIngredient) {
        return asRecipe.from(
                record.getId(), record.getName(), record.getChefId(),
                ingredients().map(it -> asUsedIngredient.from(
                        it.getId(), it.getName(), it.getQuantity(),
                        it.getRecipeId(), it.getChefId())));
    }

    public interface As<R, I> {
        R from(final Long id, final String name, final Long chefId,
                final Stream<I> ingredients);
    }
}
