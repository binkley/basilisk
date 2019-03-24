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

    public Kind unsaved(final BigDecimal coolness) {
        return new Kind(store.unsaved(coolness));
    }

    public Optional<Kind> byId(final Long id) {
        return store.byId(id)
                .map(Kind::new);
    }

    public Stream<Kind> all() {
        return store.all()
                .map(Kind::new);
    }
}
