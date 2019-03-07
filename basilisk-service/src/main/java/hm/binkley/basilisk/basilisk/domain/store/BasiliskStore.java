package hm.binkley.basilisk.basilisk.domain.store;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BasiliskStore {
    private final BasiliskRepository springData;

    public Optional<BasiliskRecord> byId(final Long id) {
        final Optional<BasiliskRecord> record = springData.findById(id);
        record.ifPresent(it -> it.store = this);
        return record;
    }

    public Stream<BasiliskRecord> byWord(final String word) {
        return springData.findByWord(word)
                .peek(it -> it.store = this);
    }

    public Stream<BasiliskRecord> all() {
        return springData.readAll()
                .peek(it -> it.store = this);
    }

    public BasiliskRecord create(final String word, final Instant at) {
        final BasiliskRecord record = BasiliskRecord.raw(word, at);
        record.store = this;
        return record.save();
    }

    public BasiliskRecord save(final BasiliskRecord record) {
        return springData.save(record);
    }
}