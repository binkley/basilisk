package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UsedIngredient
        extends Ingredient {
    public UsedIngredient(final IngredientRecord record) {
        super(record);
    }

    public <T> T asUsed(final UsedIngredient.As<T> asOther) {
        return asOther
                .from(getId(), getReceivedAt(), getName(), getRecipeId());
    }

    public interface As<T> {
        T from(final Long id, final Instant receivedAt, final String name,
                final Long recipeId);
    }
}
