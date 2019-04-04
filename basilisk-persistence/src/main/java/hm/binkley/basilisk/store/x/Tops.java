package hm.binkley.basilisk.store.x;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class Tops {
    private final TopStore store;
    private final Middles middles;

    public Top unsaved(final String code, final String name) {
        return bind(store.unsaved(code, name));
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
        return new Top(record, middles);
    }
}
