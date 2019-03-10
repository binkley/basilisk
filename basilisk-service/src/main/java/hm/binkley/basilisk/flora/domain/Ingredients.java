package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.IngredientRecord;
import hm.binkley.basilisk.flora.domain.store.IngredientStore;
import hm.binkley.basilisk.flora.rest.IngredientRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Ingredients {
    private static final IngredientRequest.As<IngredientRecord>
            asIngredientRecord = IngredientRecord::raw;

    private final IngredientStore store;

    public Optional<Ingredient> byId(final Long id) {
        return store.byId(id).map(Ingredient::new);
    }

    public Stream<Ingredient> byName(final String name) {
        return store.byName(name).map(Ingredient::new);
    }

    public Stream<Ingredient> all() {
        return store.all().map(Ingredient::new);
    }

    public Stream<Ingredient> unused() {
        return store.unused().map(Ingredient::new);
    }

    public Ingredient create(final IngredientRequest request) {
        return new Ingredient(store.save(request.as(asIngredientRecord)));
    }
}
