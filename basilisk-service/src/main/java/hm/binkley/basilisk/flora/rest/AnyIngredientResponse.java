package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.Ingredient;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Builder
@Value
public final class AnyIngredientResponse {
    Long id;
    Long sourceId;
    String name;
    BigDecimal quantity;
    Long recipeId;
    Long chefId;

    static Ingredient.As<AnyIngredientResponse> with() {
        return AnyIngredientResponse::new;
    }
}
