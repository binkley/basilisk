package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.SourceRecord;
import hm.binkley.basilisk.flora.domain.store.SourceStore;
import hm.binkley.basilisk.flora.rest.SourceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toCollection;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Sources {
    private final SourceStore store;
    private final Locations locations;

    public Optional<Source> byId(final Long id) {
        return store.byId(id).map(this::from);
    }

    public Optional<Source> byName(final String name) {
        return store.byName(name).map(this::from);
    }

    public Stream<Source> all() {
        return store.all().map(this::from);
    }

    public Source create(final SourceRequest request) {
        return from(store.save(request.as(SourceRecord::unsaved)));
    }

    private Source from(final SourceRecord record) {
        return new Source(record, record.getAvailableAt().stream()
                .map(locationRef -> locations.byRef(locationRef)
                        .orElseThrow())
                .collect(toCollection(LinkedHashSet::new)));
    }
}
