package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.SourceRecord;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Set;

import static java.util.Collections.unmodifiableSet;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public final class Source {
    private final SourceRecord record;
    private final Set<Location> availableAt;

    public Set<Location> getAvailableAt() {
        return unmodifiableSet(availableAt);
    }

    public <C> C as(final As<C> asSource) {
        return asSource.from(record.getId(), record.getName());
    }

    public interface As<C> {
        C from(final Long id, final String name);
    }
}
