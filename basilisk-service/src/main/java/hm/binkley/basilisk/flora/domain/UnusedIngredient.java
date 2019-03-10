package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.Instant;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UnusedIngredient
        extends Ingredient {
    public UnusedIngredient(final IngredientRecord record) {
        super(record);
    }

    public <T> T asUnused(final UnusedIngredient.As<T> asOther) {
        return asOther.from(getId(), getReceivedAt(), getName());
    }

    public interface As<T> {
        T from(final Long id, final Instant receivedAt, final String name);
    }
}
