package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UnusedIngredient
        extends Ingredient {
    public UnusedIngredient(final IngredientRecord record) {
        super(record);
    }

    public <I> I asUnused(final UnusedIngredient.As<I> asOther) {
        return asOther.from(getId(), getName(), getChefId());
    }

    public interface As<I> {
        I from(final Long id, final String name, final Long chefId);
    }
}
