package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class UnusedIngredient
        extends Ingredient {
    public UnusedIngredient(final IngredientRecord record) {
        super(record);
    }

    public <I> I asUnused(final UnusedIngredient.As<I> asOther) {
        return asOther.from(getId(), getSourceId(), getName(), getQuantity(),
                getChefId());
    }

    public interface As<I> {
        I from(final Long id, final Long sourceId, final String name,
                final BigDecimal quantity, final Long chefId);
    }
}
