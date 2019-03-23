package hm.binkley.basilisk.flora.recipe.rest;

import hm.binkley.basilisk.flora.ingredient.rest.UsedIngredientResponse;
import hm.binkley.basilisk.flora.recipe.Recipes.As;
import lombok.Builder;
import lombok.Value;

import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.stream.Collectors.toCollection;

@Builder
@Value
public final class RecipeResponse {
    Long id;
    String code;
    String name;
    boolean dailySpecial;
    Long chefId;
    @Builder.Default
    Set<UsedIngredientResponse> ingredients = new LinkedHashSet<>();

    public static As<RecipeResponse, UsedIngredientResponse> using(
            final boolean dailySpecial) {
        return (id, code, name, chefId, ingredients) -> new RecipeResponse(
                id, code, name, dailySpecial, chefId,
                ingredients.collect(toCollection(LinkedHashSet::new)));
    }
}
