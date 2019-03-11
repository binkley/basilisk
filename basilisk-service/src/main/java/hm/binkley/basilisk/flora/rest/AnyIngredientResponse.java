package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.Ingredient;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public final class AnyIngredientResponse {
    Long id;
    String name;
    Long recipeId;

    static Ingredient.As<AnyIngredientResponse> using() {
        return AnyIngredientResponse::new;
    }
}
