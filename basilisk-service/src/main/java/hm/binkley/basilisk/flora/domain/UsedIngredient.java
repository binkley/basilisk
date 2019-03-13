package hm.binkley.basilisk.flora.domain;

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

    public <T> T asUsed(final UsedIngredient.As<T> asOther) {
        return asOther.from(getId(), getName(), getRecipeId(), getChefId());
    }

    public interface As<I> {
        I from(final Long id, final String name, final Long recipeId,
                final Long chefId);
    }
}
