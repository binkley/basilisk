package hm.binkley.basilisk.x.near;

import hm.binkley.basilisk.x.near.store.NearRecord;
import hm.binkley.basilisk.x.near.store.NearStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class Nears {
    private final NearStore store;

    public Near unsaved(final String code) {
        return unsavedSequenced(code, 0);
    }

    public Near unsavedSequenced(
            final String code, final long sequenceNumber) {
        return bind(store.unsaved(code, sequenceNumber));
    }

    public boolean exists(final String code) {
        return store.exists(code);
    }

    public Optional<Near> byCode(final String code) {
        return store.byCode(code)
                .map(this::bind);
    }

    public Stream<Near> all() {
        return store.all()
                .map(this::bind);
    }

    private Near bind(final NearRecord record) {
        return new Near(record);
    }
}
