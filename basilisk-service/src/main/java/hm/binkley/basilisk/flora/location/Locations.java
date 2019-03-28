package hm.binkley.basilisk.flora.location;

import hm.binkley.basilisk.StandardFactory;
import hm.binkley.basilisk.flora.location.store.LocationRecord;
import hm.binkley.basilisk.flora.location.store.LocationRepository;
import hm.binkley.basilisk.flora.location.store.LocationStore;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Locations
        extends StandardFactory<LocationRecord, LocationRepository,
        LocationStore, Location> {
    public Locations(final LocationStore store) {
        super(Location::new, store);
    }

    public Location unsaved(final String code, final String name) {
        return new Location(store.unsaved(code, name));
    }

    public Optional<Location> byName(final String name) {
        return store.byName(name).map(Location::new);
    }
}
