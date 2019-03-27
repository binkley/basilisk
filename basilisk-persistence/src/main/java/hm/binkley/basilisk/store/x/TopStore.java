package hm.binkley.basilisk.store.x;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

import static hm.binkley.basilisk.store.AutoClosingStream.autoClosing;
import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor
public class TopStore {
    private final TopRepository springData;

    public TopRecord unsaved(final String name) {
        return bind(TopRecord.unsaved(name));
    }

    public Optional<TopRecord> byId(final Long id) {
        requireNonNull(id);
        return springData.findById(id)
                .map(this::bind);
    }

    public Stream<TopRecord> all() {
        return autoClosing(springData.readAll())
                .map(this::bind);
    }

    public TopRecord save(final TopRecord record) {
        return springData.save(record);
    }

    public void delete(final TopRecord record) {
        springData.delete(record);
        record.id = null;
    }

    private TopRecord bind(final TopRecord record) {
        record.store = this;
        return record;
    }
}
