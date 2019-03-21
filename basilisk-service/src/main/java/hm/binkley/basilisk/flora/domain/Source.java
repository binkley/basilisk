package hm.binkley.basilisk.flora.domain;

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

    public <S, L> S as(final Sources.As<S, L> toSource,
            final Locations.As<L> toLocation) {
        return toSource.from(getId(), getName(),
                getAvailableAt().map(it -> it.as(toLocation)));
    }
}
