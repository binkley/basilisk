package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import hm.binkley.basilisk.flora.domain.store.RecipeRecord;
import hm.binkley.basilisk.flora.domain.store.RecipeStore;
import hm.binkley.basilisk.flora.rest.RecipeRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Recipes {
    private static final RecipeRequest.As<RecipeRecord, IngredientRecord>
            asRecipeRecord = (name, chefId, ingredients) ->
            RecipeRecord.raw(name, chefId).addAll(ingredients);

    private final RecipeStore store;

    public Optional<Recipe> byId(final Long id) {
        return store.byId(id).map(Recipe::new);
    }

    public Optional<Recipe> byName(final String name) {
        return store.byName(name).map(Recipe::new);
    }

    public Stream<Recipe> all() {
        return store.all().map(Recipe::new);
    }

    public Recipe create(final RecipeRequest request) {
        return new Recipe(store.save(request.as(
                asRecipeRecord, IngredientRecord::raw)));
    }
}
