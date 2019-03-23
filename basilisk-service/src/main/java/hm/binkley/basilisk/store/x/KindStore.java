package hm.binkley.basilisk.store.x;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class KindStore {
    private final KindRepository springData;

    public KindRecord unsaved(final BigDecimal coolness) {
        return bind(KindRecord.unsaved(coolness));
    }

    public Optional<KindRecord> byId(final Long id) {
        return springData.findById(id)
                .map(this::bind);
    }

    public Stream<KindRecord> all() {
        return springData.readAll()
                .map(this::bind);
    }

    public KindRecord save(final KindRecord record) {
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
