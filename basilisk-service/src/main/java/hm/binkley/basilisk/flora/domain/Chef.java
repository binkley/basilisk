package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.ChefRecord;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public final class Chef {
    private final ChefRecord record;

    public <C> C as(final Chef.As<C> asChef) {
        return asChef.from(record.getId(), record.getName());
    }

    public interface As<C> {
        C from(final Long id, final String name);
    }
}
