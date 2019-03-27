package hm.binkley.basilisk.store.x;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

import static hm.binkley.basilisk.store.AutoClosingStream.autoClosing;
import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor
public class MiddleStore {
    private final MiddleRepository springData;

    public MiddleRecord unsaved(final int mid) {
        return bind(MiddleRecord.unsaved(mid));
    }

    public Optional<MiddleRecord> byId(final Long id) {
        requireNonNull(id);
        return springData.findById(id)
                .map(this::bind);
    }

    public Stream<MiddleRecord> all() {
        return autoClosing(springData.readAll())
                .map(this::bind);
    }

    public Stream<MiddleRecord> allOwned() {
        return autoClosing(springData.findAllOwned())
                .map(this::bind);
    }

    public Stream<MiddleRecord> allFree() {
        return autoClosing(springData.findAllFree())
                .map(this::bind);
    }

    public MiddleRecord save(final MiddleRecord record) {
        return springData.save(record);
    }

    public void delete(final MiddleRecord record) {
        springData.delete(record);
        record.id = null;
    }

    private MiddleRecord bind(final MiddleRecord record) {
        record.store = this;
        return record;
    }
}
