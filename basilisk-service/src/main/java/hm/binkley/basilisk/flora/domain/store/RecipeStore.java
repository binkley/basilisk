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
        return springData.findById(id)
                .map(this::assign);
    }

    public Optional<RecipeRecord> byName(final String name) {
        return springData.findByName(name)
                .map(this::assign);
    }

    public Stream<RecipeRecord> all() {
        return springData.readAll()
                .peek(it -> it.store = this);
    }

    public RecipeRecord create(final String name, final Long chefId) {
        final RecipeRecord record = RecipeRecord.raw(name, chefId);
        assign(record);
        return record.save();
    }

    public RecipeRecord save(final RecipeRecord record) {
        // NB -- Saving does not update ref fields in children,
        // but reading it back does
        final var saved = springData.save(record);

        // TODO: Alternative -- Manually copy in recipeId to ingredients
        // This avoids the extra DB call, but hides if something went wrong
        return springData.findById(saved.getId()).orElseThrow();
    }

    private RecipeRecord assign(final RecipeRecord record) {
        record.store = this;
        return record;
    }
}
