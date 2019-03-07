package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public class Ingredient {
    private final IngredientRecord record;

    public <T> T as(final Ingredient.As<T> asIngredient) {
        return asIngredient.from(
                record.getId(), record.getReceivedAt(), record.getName());
    }

    public interface As<T> {
        T from(final Long id, final Instant receivedAt, final String name);
    }
}
