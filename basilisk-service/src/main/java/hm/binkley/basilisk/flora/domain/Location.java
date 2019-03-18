package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.LocationRecord;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public final class Location {
    private final LocationRecord record;

    public <L> L as(final Location.As<L> asLocation) {
        return asLocation.from(record.getId(), record.getName());
    }

    public interface As<L> {
        L from(final Long id, final String name);
    }
}
