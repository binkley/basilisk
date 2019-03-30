package hm.binkley.basilisk.flora.recipe;

import hm.binkley.basilisk.StandardDomain;
import hm.binkley.basilisk.flora.ingredient.Ingredients.AsUsed;
import hm.binkley.basilisk.flora.ingredient.UsedIngredient;
import hm.binkley.basilisk.flora.recipe.Recipes.As;
import hm.binkley.basilisk.flora.recipe.store.RecipeRecord;
import hm.binkley.basilisk.flora.recipe.store.RecipeRepository;
import hm.binkley.basilisk.flora.recipe.store.RecipeStore;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.stream.Stream;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class Recipe
        extends StandardDomain<RecipeRecord, RecipeRepository, RecipeStore,
        Recipe> {
    public Recipe(final RecipeRecord record) {
        super(record);
    }

    public String getName() { return record.getName(); }

    /** @todo Ref, not record id */
    public Long getChefId() { return record.getChefId(); }

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
