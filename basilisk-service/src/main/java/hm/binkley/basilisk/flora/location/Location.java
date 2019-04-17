package hm.binkley.basilisk.flora.location;

import hm.binkley.basilisk.StandardDomain;
import hm.binkley.basilisk.flora.location.store.LocationRecord;
import hm.binkley.basilisk.flora.location.store.LocationRepository;
import hm.binkley.basilisk.flora.location.store.LocationStore;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.function.Consumer;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class Location
        extends StandardDomain<LocationRecord, LocationRepository,
        LocationStore, Location> {
    public Location(final LocationRecord record) {
        super(record);
    }

    public String getName() {
        return record.getName();
    }

    public Location insertInto(final Consumer<LocationRecord> into) {
        into.accept(record);
        return this;
    }
}
