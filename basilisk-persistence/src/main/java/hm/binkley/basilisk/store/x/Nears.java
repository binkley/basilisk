package hm.binkley.basilisk.store.x;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class Nears {
    private final NearStore store;

    public Near unsaved(final String code) {
        return bind(store.unsaved(code));
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
