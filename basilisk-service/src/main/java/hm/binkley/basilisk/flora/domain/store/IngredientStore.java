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
        final Optional<IngredientRecord> record = springData.findById(id);
        record.ifPresent(it -> it.store = this);
        return record;
    }

    public Stream<IngredientRecord> byName(final String name) {
        return springData.findByName(name)
                .peek(it -> it.store = this);
    }

    public Stream<IngredientRecord> unallocated() {
        return springData.findAllByRecipeIdIsNull()
                .peek(it -> it.store = this);
    }

    public Stream<IngredientRecord> all() {
        return springData.readAll()
                .peek(it -> it.store = this);
    }

    public IngredientRecord create(final String name) {
        final IngredientRecord record = IngredientRecord.raw(name);
        record.store = this;
        return record.save();
    }

    public IngredientRecord save(final IngredientRecord record) {
        return springData.save(record);
    }
}
