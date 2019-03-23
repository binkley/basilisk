package hm.binkley.basilisk.flora.location;

import hm.binkley.basilisk.flora.location.store.LocationRecord;
import hm.binkley.basilisk.flora.location.store.LocationRecord.LocationRef;
import hm.binkley.basilisk.flora.location.store.LocationStore;
import hm.binkley.basilisk.flora.location.rest.LocationRequest;
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

    public Optional<Location> byRef(final LocationRef ref) {
        return byId(ref.getLocationId());
    }

    public Optional<Location> byCode(final String code) {
        return store.byCode(code).map(Location::new);
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

    public interface As<L> {
        L from(final Long id, final String code, final String name);
    }
}
