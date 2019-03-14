package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.UnusedIngredient;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Builder
@Value
public final class UnusedIngredientResponse {
    Long id;
    String name;
    BigDecimal quantity;
    Long chefId;

    static UnusedIngredient.As<UnusedIngredientResponse> using() {
        return UnusedIngredientResponse::new;
    }
}
