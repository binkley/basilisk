package hm.binkley.basilisk.flora.ingredient;

import hm.binkley.basilisk.flora.ingredient.Ingredients.AsUsed;
import hm.binkley.basilisk.flora.ingredient.store.IngredientRecord;
import hm.binkley.basilisk.flora.source.Sources;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class UsedIngredient
        extends Ingredient {
    public UsedIngredient(
            final IngredientRecord record, final Sources sources) {
        super(record, sources);
    }

    public <T> T asUsed(final AsUsed<T> toIngredient) {
        return toIngredient.from(
                getId(), getCode(), getSourceId(), getName(), getQuantity(),
                getRecipeId(), getChefId());
    }
}
