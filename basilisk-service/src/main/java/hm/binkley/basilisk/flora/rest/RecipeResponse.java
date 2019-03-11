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
    @Builder.Default
    Set<UsedIngredientResponse> ingredients = new LinkedHashSet<>();

    static Recipe.As<RecipeResponse, UsedIngredientResponse> using() {
        return (id, name, ingredients) ->
                new RecipeResponse(id, name, ingredients
                        .collect(toCollection(LinkedHashSet::new)));
    }
}
