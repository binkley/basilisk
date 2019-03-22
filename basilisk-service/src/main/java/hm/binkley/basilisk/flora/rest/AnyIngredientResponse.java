package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.Ingredients.AsAny;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Builder
@Value
public final class AnyIngredientResponse {
    Long id;
    String code;
    Long sourceId;
    String name;
    BigDecimal quantity;
    Long recipeId;
    Long chefId;

    static AsAny<AnyIngredientResponse> with() {
        return AnyIngredientResponse::new;
    }
}
