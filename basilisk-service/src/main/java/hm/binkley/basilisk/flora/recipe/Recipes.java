package hm.binkley.basilisk.flora.recipe;

import hm.binkley.basilisk.flora.ingredient.store.IngredientRecord;
import hm.binkley.basilisk.flora.recipe.rest.RecipeRequest;
import hm.binkley.basilisk.flora.recipe.store.RecipeRecord;
import hm.binkley.basilisk.flora.recipe.store.RecipeStore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Recipes {
    private static final RecipeRequest.As<RecipeRecord, IngredientRecord>
            asRecipeRecord = (code, name, chefId, ingredients) ->
            RecipeRecord.unsaved(code, name, chefId).addAll(ingredients);

    private final RecipeStore store;

    public Optional<Recipe> byId(final Long id) {
        return store.byId(id).map(Recipe::new);
    }

    public Optional<Recipe> byCode(final String code) {
        return store.byCode(code).map(Recipe::new);
    }

    public Optional<Recipe> byName(final String name) {
        return store.byName(name).map(Recipe::new);
    }

    public Stream<Recipe> all() {
        return store.all().map(Recipe::new);
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
