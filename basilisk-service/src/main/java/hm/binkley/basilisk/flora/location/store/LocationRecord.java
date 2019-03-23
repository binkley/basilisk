package hm.binkley.basilisk.flora.location.store;

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
        extends StandardRecord<LocationRecord, LocationRepository,
        LocationStore> {
    @Getter
    public String name;

    public LocationRecord(final Long id, final Instant receivedAt,
            final String code, final String name) {
        super(() -> new LocationRecord(id, receivedAt, code, name),
                id, receivedAt, code);
        this.name = name;
    }

    public static LocationRecord unsaved(
            final String code, final String name) {
        return new LocationRecord(null, null, code, name);
    }

    public LocationRef ref() {
        final var ref = new LocationRef();
        ref.locationId = id;
        return ref;
    }

    @EqualsAndHashCode
    @Table("FLORA.SOURCE_LOCATION")
    @ToString
    public static class LocationRef {
        @Getter
        public Long locationId;
    }
}
