package hm.binkley.basilisk.flora.location;

import hm.binkley.basilisk.flora.location.store.LocationRecord;
import hm.binkley.basilisk.flora.location.Locations.As;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public final class Location {
    private final LocationRecord record;

    public Long getId() {
        return record.getId();
    }

    public String getCode() {
        return record.getCode();
    }

    public String getName() {
        return record.getName();
    }

    public <L> L as(final As<L> toLocation) {
        return toLocation.from(getId(), getCode(), getName());
    }
}
