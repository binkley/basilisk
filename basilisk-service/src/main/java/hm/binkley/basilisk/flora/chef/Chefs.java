package hm.binkley.basilisk.flora.chef;

import hm.binkley.basilisk.StandardFactory;
import hm.binkley.basilisk.flora.chef.store.ChefRecord;
import hm.binkley.basilisk.flora.chef.store.ChefRepository;
import hm.binkley.basilisk.flora.chef.store.ChefStore;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Chefs
        extends StandardFactory<ChefRecord, ChefRepository, ChefStore, Chef> {
    public Chefs(final ChefStore store) {
        super(store, Chef::new);
    }

    public Chef unsaved(final String code, final String name) {
        return new Chef(store.unsaved(code, name));
    }

    public Optional<Chef> byName(final String name) {
        return store.byName(name).map(binder);
    }
}
