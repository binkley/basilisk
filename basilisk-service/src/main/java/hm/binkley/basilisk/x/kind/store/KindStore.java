package hm.binkley.basilisk.x.kind.store;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

import static hm.binkley.basilisk.AutoClosingStream.autoClosing;
import static java.util.Objects.requireNonNull;

@Component
@RequiredArgsConstructor
public class KindStore {
    private final KindRepository springData;

    public KindRecord unsaved(final String code, final BigDecimal coolness) {
        return bind(KindRecord.unsaved(code, coolness));
    }

    public Optional<KindRecord> byCode(final String code) {
        requireNonNull(code);
        return springData.findById(code)
                .map(this::bind);
    }

    public Stream<KindRecord> all() {
        return autoClosing(springData.readAll())
                .map(this::bind);
    }

    public KindRecord save(final KindRecord record) {
        springData.upsert(record.code, record.coolness);
        return springData.save(record);
    }

    public void delete(final KindRecord record) {
        springData.delete(record);
    }

    private KindRecord bind(final KindRecord record) {
        record.store = this;
        return record;
    }
}
