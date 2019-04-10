package hm.binkley.basilisk.x.middle.store;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

import static hm.binkley.basilisk.AutoClosingStream.autoClosing;
import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor
public class MiddleStore {
    private final MiddleRepository springData;

    public MiddleRecord unsaved(final String code, final int mid) {
        return bind(MiddleRecord.unsaved(code, mid));
    }

    public Optional<MiddleRecord> byCode(final String code) {
        requireNonNull(code);
        return springData.findById(code)
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
        springData.upsert(record.code, record.kindCode, record.mid);
        return springData.save(record);
    }

    public void delete(final MiddleRecord record) {
        springData.delete(record);
    }

    private MiddleRecord bind(final MiddleRecord record) {
        record.store = this;
        return record;
    }
}
