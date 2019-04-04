package hm.binkley.basilisk.store.x;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class Kinds {
    private final KindStore store;

    public Kind unsaved(final String code, final BigDecimal coolness) {
        return new Kind(store.unsaved(code, coolness));
    }

    public Optional<Kind> byCode(final String code) {
        return store.byCode(code)
                .map(Kind::new);
    }

    public Stream<Kind> all() {
        return store.all()
                .map(Kind::new);
    }
}
