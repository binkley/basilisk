package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.UnusedIngredient;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public final class UnusedIngredientResponse {
    Long id;
    String name;

    static UnusedIngredient.As<UnusedIngredientResponse> using() {
        return UnusedIngredientResponse::new;
    }
}
