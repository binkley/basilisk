package hm.binkley.basilisk.x.top;

import hm.binkley.basilisk.x.middle.Middles;
import hm.binkley.basilisk.x.near.Nears;
import hm.binkley.basilisk.x.side.Side;
import hm.binkley.basilisk.x.side.Sides;
import hm.binkley.basilisk.x.top.store.TopRecord;
import hm.binkley.basilisk.x.top.store.TopStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class Tops {
    private final TopStore store;
    private final Middles middles;
    private final Sides sides;
    private final Nears nears;

    public Top unsaved(
            final String code, final String name, final Side side,
            final boolean planned) {
        return side.applyTo(sideRecord ->
                bind(store.unsaved(code, name, sideRecord, planned)));
    }

    public Optional<Top> byCode(final String code) {
        return store.byCode(code)
                .map(this::bind);
    }

    public Stream<Top> all() {
        return store.all()
                .map(this::bind);
    }

    private Top bind(final TopRecord record) {
        return new Top(record, middles, sides, nears);
    }
}
