package hm.binkley.basilisk.flora.chef.store;

import hm.binkley.basilisk.store.StandardStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ChefStore
        extends StandardStore<ChefRecord, ChefRepository, ChefStore> {
    @Autowired
    public ChefStore(final ChefRepository springData) {
        super(springData);
    }

    public ChefRecord unsaved(final String code, final String name) {
        return bind(ChefRecord.unsaved(code, name));
    }

    public Optional<ChefRecord> byName(final String name) {
        return springData.findByName(name)
                .map(this::bind);
    }
}
