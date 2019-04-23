package hm.binkley.basilisk.x.side;

import hm.binkley.basilisk.x.side.store.SideRecord;
import hm.binkley.basilisk.x.side.store.SideStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class Sides {
    private final SideStore store;

    public Side unsaved(final String code) {
        return unsavedSequenced(code, 0);
    }

    public Side unsavedSequenced(final String code,
            final long sequenceNumber) {
        return bind(store.unsaved(code, sequenceNumber));
    }

    public Optional<Side> byCode(final String code) {
        return store.byCode(code)
                .map(this::bind);
    }

    public Stream<Side> all() {
        return store.all()
                .map(this::bind);
    }

    private Side bind(final SideRecord record) {
        return new Side(record);
    }
}
