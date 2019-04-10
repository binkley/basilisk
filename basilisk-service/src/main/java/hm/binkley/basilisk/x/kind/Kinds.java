package hm.binkley.basilisk.x.kind;

import hm.binkley.basilisk.x.kind.store.KindRecord;
import hm.binkley.basilisk.x.kind.store.KindStore;
import hm.binkley.basilisk.x.near.Nears;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class Kinds {
    private final KindStore store;
    private final Nears nears;

    public Kind unsaved(final String code, final BigDecimal coolness) {
        return bind(store.unsaved(code, coolness));
    }

    public Optional<Kind> byCode(final String code) {
        return store.byCode(code)
                .map(this::bind);
    }

    public Stream<Kind> all() {
        return store.all()
                .map(this::bind);
    }

    private Kind bind(final KindRecord record) {
        return new Kind(record, nears);
    }
}
