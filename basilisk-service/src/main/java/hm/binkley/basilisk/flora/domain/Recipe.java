package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.RecipeRecord;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Instant;
import java.util.stream.Stream;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public class Recipe {
    private final RecipeRecord record;

    public Stream<Ingredient> ingredients() {
        return record.getIngredients().stream()
                .map(Ingredient::new);
    }

    public <T, U> T as(final Recipe.As<T, U> asRecipe,
            final Ingredient.As<U> asIngredient) {
        return asRecipe.from(
                record.getId(), record.getReceivedAt(), record.getName(),
                ingredients().map(it -> asIngredient.from(
                        it.getId(), it.getReceivedAt(), it.getName())));
    }

    public interface As<T, U> {
        T from(final Long id, final Instant receivedAt, final String name,
                final Stream<U> ingredients);
    }
}
