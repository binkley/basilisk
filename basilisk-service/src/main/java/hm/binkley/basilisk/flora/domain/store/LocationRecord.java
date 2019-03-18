package hm.binkley.basilisk.flora.domain.store;

import hm.binkley.basilisk.store.StandardRecord;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;

@EqualsAndHashCode(callSuper = false)
@Table("FLORA.LOCATION")
@ToString(callSuper = true)
public final class LocationRecord
        extends StandardRecord<LocationRecord, LocationRepository, LocationStore> {
    @Getter
    public String name;

    public LocationRecord(final Long id, final Instant receivedAt,
            final String name) {
        super(() -> new LocationRecord(id, receivedAt, name), id, receivedAt);
        this.name = name;
    }

    public static LocationRecord unsaved(final String name) {
        return new LocationRecord(null, null, name);
    }
}
