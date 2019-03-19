package hm.binkley.basilisk.flora.domain.store;

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
                .map(this::assign);
    }

    public LocationRecord create(final String name) {
        final LocationRecord record = LocationRecord.unsaved(name);
        assign(record);
        return record.save();
    }
}