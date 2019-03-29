package hm.binkley.basilisk.flora.recipe;

import hm.binkley.basilisk.flora.ingredient.Ingredients.AsUsed;
import hm.binkley.basilisk.flora.ingredient.UsedIngredient;
import hm.binkley.basilisk.flora.recipe.Recipes.As;
import hm.binkley.basilisk.flora.recipe.store.RecipeRecord;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.stream.Stream;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public final class Recipe {
    private final RecipeRecord record;

    public Long getId() {
        return record.getId();
    }

    public String getCode() {
        return record.getCode();
    }

    public String getName() {
        return record.getName();
    }

    /** @todo Domain object, not record id */
    public Long getChefId() {
        return record.getChefId();
    }

    /** @todo Ask ingredient factory; do not do this directly */
    public Stream<UsedIngredient> getIngredients() {
        return record.getIngredients().stream()
                .map(record -> new UsedIngredient(record, null));
    }

    public <R, I> R as(final As<R, I> toRecipe,
            final AsUsed<I> toIngredient) {
        return toRecipe.from(getId(), getCode(), getName(), getChefId(),
                getIngredients().map(it -> it.asUsed(toIngredient)));
    }
}
