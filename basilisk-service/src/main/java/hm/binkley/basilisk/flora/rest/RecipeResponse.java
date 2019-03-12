package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.Recipe;
import lombok.Builder;
import lombok.Value;

import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.stream.Collectors.toCollection;

@Builder
@Value
public final class RecipeResponse {
    Long id;
    String name;
    Long chefId;
    @Builder.Default
    Set<UsedIngredientResponse> ingredients = new LinkedHashSet<>();

    static Recipe.As<RecipeResponse, UsedIngredientResponse> using() {
        return (id, name, chefId, ingredients) ->
                new RecipeResponse(id, name, chefId, ingredients
                        .collect(toCollection(LinkedHashSet::new)));
    }
}
