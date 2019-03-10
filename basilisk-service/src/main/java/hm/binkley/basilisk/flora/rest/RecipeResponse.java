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
    Set<IngredientResponse> ingredients = new LinkedHashSet<>();

    static Recipe.As<RecipeResponse, IngredientResponse> using() {
        return (id, receivedAt, name, ingredients) ->
                new RecipeResponse(id, name, ingredients
                        .collect(toCollection(LinkedHashSet::new)));
    }
}
