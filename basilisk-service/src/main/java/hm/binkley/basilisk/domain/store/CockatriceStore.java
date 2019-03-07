package hm.binkley.basilisk.domain.store;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

import static java.math.BigDecimal.TEN;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CockatriceStore {
    private final CockatriceRepository springData;

    public Optional<CockatriceRecord> byId(final Long id) {
        final Optional<CockatriceRecord> record = springData.findById(id);
        record.ifPresent(it -> it.store = this);
        return record;
    }

    public Stream<CockatriceRecord> all() {
        return springData.readAll()
                .peek(it -> it.store = this);
    }

    public CockatriceRecord create() {
        final CockatriceRecord record = CockatriceRecord.raw(TEN);
        record.store = this;
        return record.save();
    }

    public CockatriceRecord save(final CockatriceRecord record) {
        return springData.save(record);
    }
}
