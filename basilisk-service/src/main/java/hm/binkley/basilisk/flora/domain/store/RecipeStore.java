package hm.binkley.basilisk.flora.domain.store;

import hm.binkley.basilisk.store.StandardStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class RecipeStore
        extends StandardStore<RecipeRecord, RecipeRepository, RecipeStore> {
    @Autowired
    public RecipeStore(final RecipeRepository springData) {
        super(springData);
    }

    public Optional<RecipeRecord> byName(final String name) {
        return springData.findByName(name)
                .map(this::assign);
    }

    public RecipeRecord create(final String name, final Long chefId) {
        final RecipeRecord record = RecipeRecord.raw(name, chefId);
        assign(record);
        return record.save();
    }

    @Override
    public RecipeRecord save(final RecipeRecord record) {
        // NB -- Saving does not update ref fields in children,
        // but reading it back does; this makes sense given aggregate roots
        final var saved = super.save(record);

        // TODO: Alternative -- Manually copy in recipeId to ingredients
        // This avoids the extra DB call, but hides if something went wrong
        return springData.findById(saved.getId()).orElseThrow();
    }
}
