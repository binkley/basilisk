package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import hm.binkley.basilisk.flora.domain.store.IngredientStore;
import hm.binkley.basilisk.flora.rest.UnusedIngredientRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Ingredients {
    private final IngredientStore store;

    public Optional<Ingredient> byId(final Long id) {
        return store.byId(id).map(this::asUsedOrUnused);
    }

    public Stream<Ingredient> allByName(final String name) {
        return store.byName(name).map(this::asUsedOrUnused);
    }

    public Stream<Ingredient> all() {
        return store.all().map(this::asUsedOrUnused);
    }

    public Stream<UnusedIngredient> allUnused() {
        return store.unused().map(UnusedIngredient::new);
    }

    public UnusedIngredient createUnused(
            final UnusedIngredientRequest request) {
        return new UnusedIngredient(store.save(
                request.as(IngredientRecord::unsaved)));
    }

    private Ingredient asUsedOrUnused(final IngredientRecord record) {
        return record.isUsed()
                ? new UsedIngredient(record)
                : new UnusedIngredient(record);
    }

    public interface AsAny<I> {
        I from(final Long id, final Long sourceId, final String name,
                final BigDecimal quantity, final Long recipeId,
                final Long chefId);
    }

    public interface AsUnused<I> {
        I from(final Long id, final Long sourceId, final String name,
                final BigDecimal quantity, final Long chefId);
    }

    public interface AsUsed<I> {
        I from(final Long id, final Long sourceId, final String name,
                final BigDecimal quantity, final Long recipeId,
                final Long chefId);
    }
}
