package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.Ingredients.AsUnused;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Builder
@Value
public final class UnusedIngredientResponse {
    Long id;
    Long sourceId;
    String name;
    BigDecimal quantity;
    Long chefId;

    static AsUnused<UnusedIngredientResponse> with() {
        return UnusedIngredientResponse::new;
    }
}
