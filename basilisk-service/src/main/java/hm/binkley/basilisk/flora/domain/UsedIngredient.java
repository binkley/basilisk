package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.math.BigDecimal;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class UsedIngredient
        extends Ingredient {
    public UsedIngredient(final IngredientRecord record) {
        super(record);
    }

    public <T> T asUsed(final As<T> asOther) {
        return asOther.from(getId(), getSourceId(), getName(), getQuantity(),
                getRecipeId(), getChefId());
    }

    public interface As<I> {
        I from(final Long id, final Long sourceId, final String name,
                final BigDecimal quantity, final Long recipeId,
                final Long chefId);
    }
}
