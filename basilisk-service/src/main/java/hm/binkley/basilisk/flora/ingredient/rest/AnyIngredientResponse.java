package hm.binkley.basilisk.flora.ingredient.rest;

import hm.binkley.basilisk.Codeable;
import hm.binkley.basilisk.flora.ingredient.Ingredient;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Builder
@Value
public final class AnyIngredientResponse
        implements Codeable<AnyIngredientResponse> {
    Long id;
    String code;
    Long sourceId;
    String name;
    BigDecimal quantity;
    Long recipeId;
    Long chefId;

    public static AnyIngredientResponse of(final Ingredient ingredient) {
        return new AnyIngredientResponse(
                ingredient.getId(), ingredient.getCode(),
                ingredient.getSourceId(), ingredient.getName(),
                ingredient.getQuantity(), ingredient.getRecipeId(),
                ingredient.getChefId());
    }
}
