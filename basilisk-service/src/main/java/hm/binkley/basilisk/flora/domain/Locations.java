package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.LocationRecord;
import hm.binkley.basilisk.flora.domain.store.LocationStore;
import hm.binkley.basilisk.flora.rest.LocationRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Locations {
    private final LocationStore store;

    public Optional<Location> byId(final Long id) {
        return store.byId(id).map(Location::new);
    }

    public Optional<Location> byName(final String name) {
        return store.byName(name).map(Location::new);
    }

    public Stream<Location> all() {
        return store.all().map(Location::new);
    }

    public Location create(final LocationRequest request) {
        return new Location(store.save(request.as(LocationRecord::unsaved)));
    }
}
