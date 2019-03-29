package hm.binkley.basilisk.flora.ingredient;

import hm.binkley.basilisk.flora.ingredient.Ingredients.AsUnused;
import hm.binkley.basilisk.flora.ingredient.store.IngredientRecord;
import hm.binkley.basilisk.flora.source.Sources;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class UnusedIngredient
        extends Ingredient {
    public UnusedIngredient(
            final IngredientRecord record, final Sources sources) {
        super(record, sources);
    }

    public <I> I asUnused(final AsUnused<I> toIngredient) {
        return toIngredient.from(
                getId(), getCode(), getSourceId(), getName(), getQuantity(),
                getChefId());
    }
}
