package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.SourceRecord;
import hm.binkley.basilisk.flora.domain.store.SourceStore;
import hm.binkley.basilisk.flora.rest.SourceRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class Sources {
    private final SourceStore store;

    public Optional<Source> byId(final Long id) {
        return store.byId(id).map(Source::new);
    }

    public Optional<Source> byName(final String name) {
        return store.byName(name).map(Source::new);
    }

    public Stream<Source> all() {
        return store.all().map(Source::new);
    }

    public Source create(final SourceRequest request) {
        return new Source(store.save(request.as(SourceRecord::raw)));
    }
}
