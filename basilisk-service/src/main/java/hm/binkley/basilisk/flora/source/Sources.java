package hm.binkley.basilisk.flora.source;

import hm.binkley.basilisk.StandardFactory;
import hm.binkley.basilisk.flora.location.Locations;
import hm.binkley.basilisk.flora.source.store.SourceRecord;
import hm.binkley.basilisk.flora.source.store.SourceRepository;
import hm.binkley.basilisk.flora.source.store.SourceStore;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.function.Function;

@Component
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Sources
        extends StandardFactory<SourceRecord, SourceRepository, SourceStore,
        Source> {
    private final Locations locations;

    public Sources(final SourceStore store, final Locations locations) {
        super(binder(locations), store);
        this.locations = locations;
    }

    private static Function<SourceRecord, Source> binder(
            final Locations locations) {
        return record -> new Source(record, locations);
    }

    public Source unsaved(final String code, final String name) {
        return bind(store.unsaved(code, name));
    }

    public Optional<Source> byName(final String name) {
        return store.byName(name).map(this::bind);
    }

    private Source bind(final SourceRecord record) {
        return binder(locations).apply(record);
    }
}
