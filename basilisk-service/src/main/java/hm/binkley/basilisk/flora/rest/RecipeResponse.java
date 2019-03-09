package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.Recipe;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public final class RecipeResponse {
    Long id;
    String name;

    static Recipe.As<RecipeResponse> using() {
        return (id, receivedAt, name) ->
                new RecipeResponse(id, name);
    }
}
