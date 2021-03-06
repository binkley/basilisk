package hm.binkley.basilisk.x.top.store;

import hm.binkley.basilisk.x.side.store.SideRecord;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

import static hm.binkley.basilisk.AutoClosingStream.autoClosing;
import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor
public class TopStore {
    private final TopRepository springData;

    public TopRecord unsaved(
            final String code, final SideRecord side, final String name,
            final long sequenceNumber) {
        return bind(TopRecord.unsaved(code, side, name, sequenceNumber));
    }

    public Optional<TopRecord> byCode(final String code) {
        requireNonNull(code);
        return springData.findById(code)
                .map(this::bind);
    }

    public Stream<TopRecord> all() {
        return autoClosing(springData.readAll())
                .map(this::bind);
    }

    public TopRecord save(final TopRecord record) {
        springData.upsert(record.code, record.name, record.plannedNearCode,
                record.sequenceNumber);
        return springData.save(record);
    }

    public void delete(final TopRecord record) {
        springData.delete(record);
    }

    private TopRecord bind(final TopRecord record) {
        record.store = this;
        return record;
    }
}
