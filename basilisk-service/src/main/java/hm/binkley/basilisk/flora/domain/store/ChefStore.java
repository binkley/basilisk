package hm.binkley.basilisk.flora.domain.store;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ChefStore {
    private final ChefRepository springData;

    public Optional<ChefRecord> byId(final Long id) {
        return springData.findById(id)
                .map(this::assign);
    }

    public Optional<ChefRecord> byCode(final String code) {
        return springData.findByCode(code)
                .map(this::assign);
    }

    public Optional<ChefRecord> byName(final String name) {
        return springData.findByName(name)
                .map(this::assign);
    }

    public Stream<ChefRecord> all() {
        return springData.readAll()
                .map(this::assign);
    }

    public ChefRecord create(final String code, final String name) {
        final ChefRecord record = ChefRecord.raw(code, name);
        assign(record);
        return record.save();
    }

    public ChefRecord save(final ChefRecord record) {
        return springData.save(record);
    }

    private ChefRecord assign(final ChefRecord record) {
        record.store = this;
        return record;
    }
}
