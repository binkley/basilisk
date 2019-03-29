package hm.binkley.basilisk.flora.ingredient;

import hm.binkley.basilisk.StandardFactory;
import hm.binkley.basilisk.flora.ingredient.rest.UnusedIngredientRequest;
import hm.binkley.basilisk.flora.ingredient.store.IngredientRecord;
import hm.binkley.basilisk.flora.ingredient.store.IngredientRepository;
import hm.binkley.basilisk.flora.ingredient.store.IngredientStore;
import hm.binkley.basilisk.flora.source.Sources;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.function.Function;
import java.util.stream.Stream;

@Component
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Ingredients
        extends StandardFactory<IngredientRecord, IngredientRepository,
        IngredientStore, Ingredient> {
    private final Sources sources;

    @Autowired
    public Ingredients(final IngredientStore store, final Sources sources) {
        super(binder(sources), store);
        this.sources = sources;
    }

    private static Function<IngredientRecord, Ingredient> binder(
            final Sources sources) {
        return record -> record.isUsed()
                ? new UsedIngredient(record, sources)
                : new UnusedIngredient(record, sources);
    }

    public Ingredient unsaved(final String code, final Long sourceId,
            final String name,
            final BigDecimal quantity, final Long chefId) {
        return bind(store.unsaved(
                code, sourceId, name, quantity, chefId));
    }

    public Stream<Ingredient> allByName(final String name) {
        return store.byName(name).map(this::bind);
    }

    public Stream<UnusedIngredient> allUnused() {
        return store.unused().map(this::unusedBind);
    }

    public UnusedIngredient createUnused(
            final UnusedIngredientRequest request) {
        return new UnusedIngredient(store.save(
                request.as(IngredientRecord::unsaved)), sources);
    }

    private UnusedIngredient unusedBind(final IngredientRecord record) {
        return new UnusedIngredient(record, sources);
    }

    private Ingredient bind(final IngredientRecord record) {
        return binder(sources).apply(record);
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
