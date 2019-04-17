package hm.binkley.basilisk.flora.source;

import hm.binkley.basilisk.StandardDomain;
import hm.binkley.basilisk.flora.location.Location;
import hm.binkley.basilisk.flora.location.Locations;
import hm.binkley.basilisk.flora.source.store.SourceRecord;
import hm.binkley.basilisk.flora.source.store.SourceRecord.LocationRef;
import hm.binkley.basilisk.flora.source.store.SourceRepository;
import hm.binkley.basilisk.flora.source.store.SourceStore;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.util.Optional;
import java.util.stream.Stream;

@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class Source
        extends StandardDomain<SourceRecord, SourceRepository, SourceStore,
        Source> {
    private final Locations locations;

    public Source(final SourceRecord record, final Locations locations) {
        super(record);
        this.locations = locations;
    }

    public String getName() {
        return record.getName();
    }

    public Stream<Location> getAvailableAt() {
        return record.getAvailableAt().stream()
                .map(LocationRef::getLocationId)
                .map(locations::byId)
                .map(Optional::orElseThrow);
    }

    public Source addAvailableAt(final Stream<Location> locations) {
        locations.forEach(this::addAvailableAt);
        return this;
    }

    public Source addAvailableAt(final Location location) {
        location.insertInto(record::addAvailableAt);
        return this;
    }

    public Source removeAvailableAt(final Stream<Location> locations) {
        locations.forEach(this::removeAvailableAt);
        return this;
    }

    public Source removeAvailableAt(final Location location) {
        location.insertInto(record::removeAvailableAt);
        return this;
    }
}
