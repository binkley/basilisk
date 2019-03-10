package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public abstract class Ingredient {
    private final IngredientRecord record;

    public Long getId() {
        return record.getId();
    }

    /** @todo Rethink exposing audit fields beyond record */
    public Instant getReceivedAt() {
        return record.getReceivedAt();
    }

    public String getName() {
        return record.getName();
    }

    public Long getRecipeId() {
        return record.getRecipeId();
    }

    public <T> T asAny(final Ingredient.As<T> asOther) {
        return asOther
                .from(getId(), getReceivedAt(), getName(), getRecipeId());
    }

    public interface As<T> {
        T from(final Long id, final Instant receivedAt, final String name,
                final Long recipeId);
    }
}
