package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.Ingredients.AsUsed;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Builder
@Value
public final class UsedIngredientResponse {
    Long id;
    Long sourceId;
    String name;
    BigDecimal quantity;
    Long recipeId;
    Long chefId;

    static AsUsed<UsedIngredientResponse> with() {
        return UsedIngredientResponse::new;
    }
}
