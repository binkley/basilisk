package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.RecipeRecord;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public class Recipe {
    private final RecipeRecord record;

    public <T> T as(final Recipe.As<T> asRecipe) {
        return asRecipe.from(record.getId(), record.getReceivedAt(),
                record.getName());
    }

    public interface As<T> {
        T from(final Long id, final Instant receivedAt, final String name);
    }
}
