package hm.binkley.basilisk.flora.ingredient.store;

import hm.binkley.basilisk.store.StandardStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static hm.binkley.basilisk.AutoClosingStream.autoClosing;

@Component
public class IngredientStore
        extends StandardStore<IngredientRecord, IngredientRepository,
        IngredientStore> {
    @Autowired
    public IngredientStore(final IngredientRepository springData) {
        super(springData);
    }

    public Stream<IngredientRecord> byName(final String name) {
        return autoClosing(springData.findAllByName(name))
                .map(this::bind);
    }

    public Stream<IngredientRecord> unused() {
        return autoClosing(springData.findAllByRecipeIdIsNull())
                .peek(it -> it.store = this);
    }

    public IngredientRecord create(
            final String code, final Long sourceId, final String name,
            final BigDecimal quantity, final Long chefId) {
        final IngredientRecord record = IngredientRecord.unsaved(
                code, sourceId, name, quantity, chefId);
        bind(record);
        return record.save();
    }
}
