package hm.binkley.basilisk.flora.domain.store;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class IngredientStore {
    private final IngredientRepository springData;

    public Optional<IngredientRecord> byId(final Long id) {
        return springData.findById(id)
                .map(this::assign);
    }

    public Stream<IngredientRecord> byName(final String name) {
        return springData.findAllByName(name)
                .map(this::assign);
    }

    public Stream<IngredientRecord> unused() {
        return springData.findAllByRecipeIdIsNull()
                .peek(it -> it.store = this);
    }

    public Stream<IngredientRecord> all() {
        return springData.readAll()
                .peek(it -> it.store = this);
    }

    public IngredientRecord create(final String name, final Long chefId) {
        final IngredientRecord record = IngredientRecord.raw(name, chefId);
        assign(record);
        return record.save();
    }

    public IngredientRecord save(final IngredientRecord record) {
        return springData.save(record);
    }

    private IngredientRecord assign(final IngredientRecord record) {
        record.store = this;
        return record;
    }
}
