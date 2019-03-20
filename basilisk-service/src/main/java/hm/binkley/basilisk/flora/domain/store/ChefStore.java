package hm.binkley.basilisk.flora.domain.store;

import hm.binkley.basilisk.store.StandardStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ChefStore
        extends StandardStore<ChefRecord, ChefRepository, ChefStore> {
    @Autowired
    public ChefStore(final ChefRepository springData) {
        super(springData);
    }

    public Optional<ChefRecord> byCode(final String code) {
        return springData.findByCode(code)
                .map(this::assign);
    }

    public Optional<ChefRecord> byName(final String name) {
        return springData.findByName(name)
                .map(this::assign);
    }

    public ChefRecord create(final String code, final String name) {
        final ChefRecord record = ChefRecord.unsaved(code, name);
        assign(record);
        return record.save();
    }
}
