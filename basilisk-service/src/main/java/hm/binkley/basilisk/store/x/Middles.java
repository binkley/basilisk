package hm.binkley.basilisk.store.x;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class Middles {
    private final MiddleStore store;
    private final Kinds kinds;

    public Middle unsaved(final int mid) {
        return new Middle(store.unsaved(mid), kinds);
    }

    public Stream<Middle> all() {
        return store.all()
                .map(this::bind);
    }

    public Stream<Middle> owned() {
        return store.allOwned()
                .map(this::bind);
    }

    public Stream<Middle> free() {
        return store.allFree()
                .map(this::bind);
    }

    public Optional<Middle> byId(final Long id) {
        return store.byId(id)
                .map(this::bind);
    }

    private Middle bind(final MiddleRecord record) {
        return new Middle(record, kinds);
    }
}
