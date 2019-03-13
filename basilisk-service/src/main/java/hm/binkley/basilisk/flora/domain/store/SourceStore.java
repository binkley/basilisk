package hm.binkley.basilisk.flora.domain.store;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SourceStore {
    private final SourceRepository springData;

    public Optional<SourceRecord> byId(final Long id) {
        return springData.findById(id)
                .map(this::assign);
    }

    public Optional<SourceRecord> byName(final String name) {
        return springData.findByName(name)
                .map(this::assign);
    }

    public Stream<SourceRecord> all() {
        return springData.readAll()
                .peek(it -> it.store = this);
    }

    public SourceRecord create(final String name) {
        final SourceRecord record = SourceRecord.raw(name);
        assign(record);
        return record.save();
    }

    public SourceRecord save(final SourceRecord record) {
        return springData.save(record);
    }

    private SourceRecord assign(final SourceRecord record) {
        record.store = this;
        return record;
    }
}
