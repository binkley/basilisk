package hm.binkley.basilisk.flora.domain.store;

import hm.binkley.basilisk.store.StandardStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.stream.Stream;

@Component
public class IngredientStore
        extends StandardStore<IngredientRecord, IngredientRepository,
        IngredientStore> {
    @Autowired
    public IngredientStore(final IngredientRepository springData) {
        super(springData);
    }

    public Stream<IngredientRecord> byName(final String name) {
        return springData.findAllByName(name)
                .map(this::assign);
    }

    public Stream<IngredientRecord> unused() {
        return springData.findAllByRecipeIdIsNull()
                .peek(it -> it.store = this);
    }

    public IngredientRecord create(
            final String name, final BigDecimal quantity, final Long chefId) {
        final IngredientRecord record = IngredientRecord.raw(
                name, quantity, chefId);
        assign(record);
        return record.save();
    }
}
