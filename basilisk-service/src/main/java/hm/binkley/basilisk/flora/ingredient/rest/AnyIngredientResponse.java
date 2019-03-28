package hm.binkley.basilisk.flora.ingredient.rest;

import hm.binkley.basilisk.flora.ingredient.Ingredients.AsAny;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Builder
@Value
public final class AnyIngredientResponse
        implements Comparable<AnyIngredientResponse> {
    Long id;
    String code;
    Long sourceId;
    String name;
    BigDecimal quantity;
    Long recipeId;
    Long chefId;

    public static AsAny<AnyIngredientResponse> using() {
        return AnyIngredientResponse::new;
    }

    @Override
    public int compareTo(final AnyIngredientResponse that) {
        return getCode().compareTo(that.getCode());
    }
}
