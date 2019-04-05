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
    private final Sides sides;
    private final Nears nears;

    public Middle unsaved(final String code, final int mid) {
        return bind(store.unsaved(code, mid));
    }

    public Optional<Middle> byCode(final String code) {
        return store.byCode(code)
                .map(this::bind);
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

    private Middle bind(final MiddleRecord record) {
        return new Middle(record, kinds, sides, nears);
    }
}
