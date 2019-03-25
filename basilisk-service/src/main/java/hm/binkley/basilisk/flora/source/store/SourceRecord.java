package hm.binkley.basilisk.flora.source.store;

import hm.binkley.basilisk.flora.location.store.LocationRecord;
import hm.binkley.basilisk.store.StandardRecord;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@EqualsAndHashCode(callSuper = false)
@Table("FLORA.SOURCE")
@ToString(callSuper = true)
public final class SourceRecord
        extends StandardRecord<SourceRecord, SourceRepository, SourceStore> {
    @Getter
    String name;
    @Column("source_id")
    @Getter
    Set<LocationRef> availableAt = new LinkedHashSet<>();

    public SourceRecord(final Long id, final Instant receivedAt,
            final String code, final String name) {
        super(id, receivedAt, code);
        this.name = name;
    }

    public static SourceRecord unsaved(final String code, final String name) {
        return new SourceRecord(null, null, code, name);
    }

    public SourceRecord addAvailableAt(
            final Stream<LocationRecord> locations) {
        locations.forEach(this::addAvailableAt);
        return this;
    }

    public SourceRecord addAvailableAt(final LocationRecord location) {
        check(location);
        final var ref = LocationRef.of(location);
        if (!availableAt.add(ref))
            throw new IllegalStateException("Duplicate: " + location);
        return this;
    }

    public SourceRecord removeAvailableAt(
            final Stream<LocationRecord> locations) {
        locations.forEach(this::removeAvailableAt);
        return this;
    }

    public SourceRecord removeAvailableAt(final LocationRecord location) {
        check(location);
        final var ref = LocationRef.of(location);
        if (!availableAt.remove(ref))
            throw new NoSuchElementException("Absent: " + location);
        return this;
    }

    private void check(final LocationRecord location) {
        requireNonNull(location);
    }

    @EqualsAndHashCode
    @Table("FLORA.SOURCE_LOCATION")
    @ToString
    public static class LocationRef {
        @Getter
        public Long locationId;

        public static LocationRef of(final LocationRecord location) {
            if (null == location.id)
                location.save();
            final var ref = new LocationRef();
            ref.locationId = location.id;
            return ref;
        }
    }
}
