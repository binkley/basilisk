package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.Sources.As;
import hm.binkley.basilisk.flora.domain.store.SourceRecord;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Optional;
import java.util.stream.Stream;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public final class Source {
    private final SourceRecord record;
    private final Locations locations;

    public Long getId() {
        return record.getId();
    }

    public String getName() {
        return record.getName();
    }

    public Stream<Location> getAvailableAt() {
        return record.getAvailableAt().stream()
                .map(locations::byRef)
                .map(Optional::orElseThrow);
    }

    public <S> S as(final As<S> toSource) {
        return toSource.from(getId(), getName());
    }
}
