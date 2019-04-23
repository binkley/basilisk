package hm.binkley.basilisk.x.side.store;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

import static hm.binkley.basilisk.AutoClosingStream.autoClosing;
import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor
public class SideStore {
    private final SideRepository springData;

    public SideRecord unsaved(final String code) {
        return bind(SideRecord.unsaved(code));
    }

    public Optional<SideRecord> byCode(final String code) {
        requireNonNull(code);
        return springData.findById(code)
                .map(this::bind);
    }

    public Stream<SideRecord> all() {
        return autoClosing(springData.readAll())
                .map(this::bind);
    }

    public SideRecord save(final SideRecord record) {
        springData.upsert(record.code);
        return springData.save(record);
    }

    public void delete(final SideRecord record) {
        springData.delete(record);
    }

    private SideRecord bind(final SideRecord record) {
        record.store = this;
        return record;
    }
}
