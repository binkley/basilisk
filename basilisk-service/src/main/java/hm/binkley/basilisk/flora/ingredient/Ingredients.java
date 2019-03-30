package hm.binkley.basilisk.flora.ingredient;

import hm.binkley.basilisk.StandardFactory;
import hm.binkley.basilisk.flora.ingredient.store.IngredientRecord;
import hm.binkley.basilisk.flora.ingredient.store.IngredientRepository;
import hm.binkley.basilisk.flora.ingredient.store.IngredientStore;
import hm.binkley.basilisk.flora.source.Sources;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Stream;

@Component
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Ingredients
        extends StandardFactory<IngredientRecord, IngredientRepository,
        IngredientStore, Ingredient> {
    @Autowired
    public Ingredients(final IngredientStore store, final Sources sources) {
        super(store, record -> record.isUsed()
                ? new UsedIngredient(record, sources)
                : new UnusedIngredient(record, sources));
    }

    public UnusedIngredient unsaved(
            final String code, final Long sourceId, final String name,
            final BigDecimal quantity, final Long chefId) {
        return (UnusedIngredient) bind(store.unsaved(
                code, sourceId, name, quantity, chefId));
    }

    public Stream<Ingredient> allByName(final String name) {
        return store.byName(name).map(binder);
    }

    public Stream<UnusedIngredient> allUnused() {
        return store.unused()
                .map(binder)
                .map(UnusedIngredient.class::cast);
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
