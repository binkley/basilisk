package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.Recipes.As;
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

    static As<RecipeResponse, UsedIngredientResponse> using(
            final boolean dailySpecial) {
        return (id, code, name, chefId, ingredients) -> new RecipeResponse(
                id, code, name, dailySpecial, chefId,
                ingredients.collect(toCollection(LinkedHashSet::new)));
    }
}
