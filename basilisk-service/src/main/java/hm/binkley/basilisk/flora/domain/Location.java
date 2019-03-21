package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.Locations.As;
import hm.binkley.basilisk.flora.domain.store.LocationRecord;
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

    public String getName() {
        return record.getName();
    }

    public <L> L as(final As<L> toLocation) {
        return toLocation.from(getId(), getName());
    }
}
