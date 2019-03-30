package hm.binkley.basilisk.flora.recipe;

import hm.binkley.basilisk.StandardFactory;
import hm.binkley.basilisk.flora.ingredient.store.IngredientRecord;
import hm.binkley.basilisk.flora.recipe.rest.RecipeRequest;
import hm.binkley.basilisk.flora.recipe.store.RecipeRecord;
import hm.binkley.basilisk.flora.recipe.store.RecipeRepository;
import hm.binkley.basilisk.flora.recipe.store.RecipeStore;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Recipes
        extends StandardFactory<RecipeRecord, RecipeRepository, RecipeStore,
        Recipe> {
    private static final RecipeRequest.As<RecipeRecord, IngredientRecord>
            asRecipeRecord = (code, name, chefId, ingredients) ->
            RecipeRecord.unsaved(code, name, chefId)
                    .addAllUnusedIngredients(ingredients);

    public Recipes(final RecipeStore store) {
        super(store, Recipe::new);
    }

    public Optional<Recipe> byName(final String name) {
        return store.byName(name).map(binder);
    }

    public Recipe create(final RecipeRequest request) {
        return new Recipe(store.save(request.as(
                asRecipeRecord, IngredientRecord::unsaved)));
    }

    public interface As<R, I> {
        R from(final Long id, final String code, final String name,
                final Long chefId, final Stream<I> ingredients);
    }
}
