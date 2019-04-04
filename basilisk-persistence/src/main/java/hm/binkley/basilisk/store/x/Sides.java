package hm.binkley.basilisk.store.x;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Optional;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class Sides {
    private final SideStore store;

    public Side unsaved(final String code, final Instant time) {
        return new Side(store.unsaved(code, time));
    }

    public Optional<Side> byCode(final String code) {
        return store.byCode(code)
                .map(Side::new);
    }

    public Stream<Side> all() {
        return store.all()
                .map(Side::new);
    }
}
