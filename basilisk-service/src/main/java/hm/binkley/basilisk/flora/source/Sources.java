package hm.binkley.basilisk.flora.source;

import hm.binkley.basilisk.flora.location.Locations;
import hm.binkley.basilisk.flora.source.rest.SourceRequest;
import hm.binkley.basilisk.flora.source.store.SourceRecord;
import hm.binkley.basilisk.flora.source.store.SourceStore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Sources {
    private final SourceStore store;
    private final Locations locations;

    public Optional<Source> byId(final Long id) {
        return store.byId(id).map(this::from);
    }

    public Optional<Source> byCode(final String code) {
        return store.byCode(code).map(this::from);
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
        return new Source(record, locations);
    }

    public interface As<S, L> {
        S from(final Long id, final String code, final String name,
                final Stream<L> availableAt);
    }
}