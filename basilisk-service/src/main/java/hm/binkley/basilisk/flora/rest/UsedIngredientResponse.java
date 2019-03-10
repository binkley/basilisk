package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.UsedIngredient;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public final class UsedIngredientResponse {
    Long id;
    String name;

    static UsedIngredient.As<UsedIngredientResponse> using() {
        return (id, receivedAt, name, recipeId) ->
                new UsedIngredientResponse(id, name);
    }
}
