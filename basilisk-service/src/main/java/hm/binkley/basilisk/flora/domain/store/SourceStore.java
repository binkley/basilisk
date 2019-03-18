package hm.binkley.basilisk.flora.domain.store;

import hm.binkley.basilisk.store.StandardStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class SourceStore
        extends StandardStore<SourceRecord, SourceRepository, SourceStore> {
    @Autowired
    public SourceStore(final SourceRepository springData) {
        super(springData);
    }

    public Optional<SourceRecord> byName(final String name) {
        return springData.findByName(name)
                .map(this::assign);
    }

    public SourceRecord create(final String name) {
        final SourceRecord record = SourceRecord.unsaved(name);
        assign(record);
        return record.save();
    }
}
