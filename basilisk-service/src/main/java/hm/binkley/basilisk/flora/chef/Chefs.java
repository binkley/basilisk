package hm.binkley.basilisk.flora.chef;

import hm.binkley.basilisk.flora.chef.store.ChefRecord;
import hm.binkley.basilisk.flora.chef.store.ChefStore;
import hm.binkley.basilisk.flora.chef.rest.ChefRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Chefs {
    private final ChefStore store;

    public Optional<Chef> byId(final Long id) {
        return store.byId(id).map(Chef::new);
    }

    public Optional<Chef> byCode(final String code) {
        return store.byCode(code).map(Chef::new);
    }

    public Optional<Chef> byName(final String name) {
        return store.byName(name).map(Chef::new);
    }

    public Stream<Chef> all() {
        return store.all().map(Chef::new);
    }

    public Chef create(final ChefRequest request) {
        return new Chef(store.save(request.as(ChefRecord::unsaved)));
    }
}
