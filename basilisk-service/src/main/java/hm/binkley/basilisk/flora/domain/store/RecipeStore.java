package hm.binkley.basilisk.flora.domain.store;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class RecipeStore {
    private final RecipeRepository springData;

    public Optional<RecipeRecord> byId(final Long id) {
        final Optional<RecipeRecord> record = springData.findById(id);
        record.ifPresent(it -> it.store = this);
        return record;
    }

    public Stream<RecipeRecord> byName(final String name) {
        return springData.findByName(name)
                .peek(it -> it.store = this);
    }

    public Stream<RecipeRecord> all() {
        return springData.readAll()
                .peek(it -> it.store = this);
    }

    public RecipeRecord create(final String name) {
        final RecipeRecord record = RecipeRecord.raw(name);
        record.store = this;
        return record.save();
    }

    public RecipeRecord save(final RecipeRecord record) {
        return springData.save(record);
    }
}
