package hm.binkley.basilisk.x.middle;

import hm.binkley.basilisk.x.kind.Kinds;
import hm.binkley.basilisk.x.middle.store.MiddleRecord;
import hm.binkley.basilisk.x.middle.store.MiddleStore;
import hm.binkley.basilisk.x.near.Nears;
import hm.binkley.basilisk.x.side.Side;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class Middles {
    private final MiddleStore store;
    private final Kinds kinds;
    private final Nears nears;

    public Middle unsaved(final String code, final Side side, final int mid) {
        return unsavedSequenced(code, side, mid, 0);
    }

    public Middle unsavedSequenced(
            final String code, final Side side, final int mid,
            final long sequenceNumber) {
        return side.applyInto(sideRecord ->
                bind(store.unsaved(code, sideRecord, mid, sequenceNumber)));
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
        return new Middle(record, kinds, nears);
    }
}
