package hm.binkley.basilisk.x.near.store;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

import static hm.binkley.basilisk.AutoClosingStream.autoClosing;
import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor
public class NearStore {
    private final NearRepository springData;

    public NearRecord unsaved(final String code) {
        return bind(NearRecord.unsaved(code));
    }

    public boolean exists(final String code) {
        return springData.existsById(code);
    }

    public Optional<NearRecord> byCode(final String code) {
        requireNonNull(code);
        return springData.findById(code)
                .map(this::bind);
    }

    public Stream<NearRecord> all() {
        return autoClosing(springData.readAll())
                .map(this::bind);
    }

    public NearRecord save(final NearRecord record) {
        springData.upsert(record.code);
        return springData.save(record);
    }

    public void delete(final NearRecord record) {
        springData.delete(record);
    }

    private NearRecord bind(final NearRecord record) {
        record.store = this;
        return record;
    }
}
