package hm.binkley.basilisk.store.x;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class MiddleStore {
    private final MiddleRepository springData;

    public MiddleRecord unsaved(final int mid) {
        return bind(MiddleRecord.unsaved(mid));
    }

    public Optional<MiddleRecord> byId(final Long id) {
        return springData.findById(id)
                .map(this::bind);
    }

    public Stream<MiddleRecord> all() {
        return springData.readAll()
                .map(this::bind);
    }

    public Stream<MiddleRecord> allOwned() {
        return springData.findAllOwned()
                .map(this::bind);
    }

    public Stream<MiddleRecord> allFree() {
        return springData.findAllFree()
                .map(this::bind);
    }

    public Stream<BottomRecord> allBottoms() {
        return springData.findAllBottoms();
    }

    public MiddleRecord save(final MiddleRecord record) {
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
