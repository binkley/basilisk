package hm.binkley.basilisk.flora.ingredient;

import hm.binkley.basilisk.StandardFactory;
import hm.binkley.basilisk.flora.ingredient.rest.UnusedIngredientRequest;
import hm.binkley.basilisk.flora.ingredient.store.IngredientRecord;
import hm.binkley.basilisk.flora.ingredient.store.IngredientRepository;
import hm.binkley.basilisk.flora.ingredient.store.IngredientStore;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Stream;

@Component
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Ingredients
        extends StandardFactory<IngredientRecord, IngredientRepository,
        IngredientStore, Ingredient> {
    public Ingredients(final IngredientStore store) {
        super(Ingredients::asUsedOrUnused, store);
    }

    private static Ingredient asUsedOrUnused(final IngredientRecord record) {
        return record.isUsed()
                ? new UsedIngredient(record)
                : new UnusedIngredient(record);
    }

    public Ingredient unsaved(final String code, final Long sourceId,
            final String name,
            final BigDecimal quantity, final Long chefId) {
        return asUsedOrUnused(store.unsaved(
                code, sourceId, name, quantity, chefId));
    }

    public Stream<Ingredient> allByName(final String name) {
        return store.byName(name).map(Ingredients::asUsedOrUnused);
    }

    public Stream<UnusedIngredient> allUnused() {
        return store.unused().map(UnusedIngredient::new);
    }

    public UnusedIngredient createUnused(
            final UnusedIngredientRequest request) {
        return new UnusedIngredient(store.save(
                request.as(IngredientRecord::unsaved)));
    }

    public interface AsAny<I> {
        I from(final Long id, final String code, final Long sourceId,
                final String name, final BigDecimal quantity,
                final Long recipeId, final Long chefId);
    }

    public interface AsUnused<I> {
        I from(final Long id, final String code, final Long sourceId,
                final String name, final BigDecimal quantity,
                final Long chefId);
    }

    public interface AsUsed<I> {
        I from(final Long id, final String code, final Long sourceId,
                final String name, final BigDecimal quantity,
                final Long recipeId, final Long chefId);
    }
}
