package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.UsedIngredient;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Builder
@Value
public final class UsedIngredientResponse {
    Long id;
    String name;
    BigDecimal quantity;
    Long recipeId;
    Long chefId;

    static UsedIngredient.As<UsedIngredientResponse> with() {
        return UsedIngredientResponse::new;
    }
}
