package hm.binkley.basilisk.flora.location.store;

import hm.binkley.basilisk.store.StandardStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LocationStore
        extends
        StandardStore<LocationRecord, LocationRepository, LocationStore> {
    @Autowired
    public LocationStore(final LocationRepository springData) {
        super(springData);
    }

    public LocationRecord unsaved(final String code, final String name) {
        return bind(LocationRecord.unsaved(code, name));
    }

    public Optional<LocationRecord> byName(final String name) {
        return springData.findByName(name)
                .map(this::bind);
    }
}
