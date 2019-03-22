package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.Ingredients.AsUnused;
import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class UnusedIngredient
        extends Ingredient {
    public UnusedIngredient(final IngredientRecord record) {
        super(record);
    }

    public <I> I asUnused(final AsUnused<I> toIngredient) {
        return toIngredient.from(
                getId(), getCode(), getSourceId(), getName(), getQuantity(),
                getChefId());
    }
}
