package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.Ingredient;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public final class IngredientResponse {
    Long id;
    String name;

    static Ingredient.As<IngredientResponse> using() {
        return (id, receivedAt, name) ->
                new IngredientResponse(id, name);
    }
}
