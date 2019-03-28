package hm.binkley.basilisk.flora.ingredient.rest;

import hm.binkley.basilisk.flora.ingredient.Ingredients.AsUsed;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

import static lombok.AccessLevel.PUBLIC;

@AllArgsConstructor(access = PUBLIC)
@Builder
@Value
public final class UsedIngredientResponse
        implements Comparable<UsedIngredientResponse> {
    Long id;
    String code;
    Long sourceId;
    String name;
    BigDecimal quantity;
    Long recipeId;
    Long chefId;

    public static AsUsed<UsedIngredientResponse> using() {
        return UsedIngredientResponse::new;
    }

    @Override
    public int compareTo(final UsedIngredientResponse that) {
        return getCode().compareTo(that.getCode());
    }
}
