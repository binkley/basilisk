package hm.binkley.basilisk.domain.store;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BasiliskStore {
    private final BasiliskRepository springData;

    public Optional<BasiliskRecord> byId(final long id) {
        final Optional<BasiliskRecord> record = springData.findById(id);
        record.ifPresent(it -> it.store = this);
        return record;
    }

    public BasiliskRecord save(final BasiliskRecord record) {
        return springData.save(record);
    }
}
