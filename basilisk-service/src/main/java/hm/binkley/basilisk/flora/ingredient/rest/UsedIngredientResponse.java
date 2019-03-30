package hm.binkley.basilisk.flora.ingredient.rest;

import hm.binkley.basilisk.Codeable;
import hm.binkley.basilisk.flora.ingredient.UsedIngredient;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Builder
@Value
public final class UsedIngredientResponse
        implements Codeable<UsedIngredientResponse> {
    Long id;
    String code;
    Long sourceId;
    String name;
    BigDecimal quantity;
    Long recipeId;
    Long chefId;

    public static UsedIngredientResponse of(final UsedIngredient domain) {
        return new UsedIngredientResponse(
                domain.getId(), domain.getCode(), domain.getSourceId(),
                domain.getName(), domain.getQuantity(), domain.getRecipeId(),
                domain.getChefId());
    }
}
