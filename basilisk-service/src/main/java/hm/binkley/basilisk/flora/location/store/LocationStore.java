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

    public Optional<LocationRecord> byName(final String name) {
        return springData.findByName(name)
                .map(this::bind);
    }

    public LocationRecord create(final String code, final String name) {
        final LocationRecord record = LocationRecord.unsaved(code, name);
        bind(record);
        return record.save();
    }
}
