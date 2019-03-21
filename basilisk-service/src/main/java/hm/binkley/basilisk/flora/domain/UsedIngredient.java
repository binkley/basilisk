package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.Ingredients.AsUsed;
import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class UsedIngredient
        extends Ingredient {
    public UsedIngredient(final IngredientRecord record) {
        super(record);
    }

    public <T> T asUsed(final AsUsed<T> toIngredient) {
        return toIngredient.from(
                getId(), getSourceId(), getName(), getQuantity(),
                getRecipeId(), getChefId());
    }
}
