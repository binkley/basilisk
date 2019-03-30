package hm.binkley.basilisk.flora.ingredient.rest;

import hm.binkley.basilisk.Codeable;
import hm.binkley.basilisk.flora.ingredient.Ingredients.AsUnused;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Builder
@Value
public final class UnusedIngredientResponse
        implements Codeable<UnusedIngredientResponse> {
    Long id;
    String code;
    Long sourceId;
    String name;
    BigDecimal quantity;
    Long chefId;

    public static AsUnused<UnusedIngredientResponse> using() {
        return UnusedIngredientResponse::new;
    }
}
