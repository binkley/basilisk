package hm.binkley.basilisk.flora.chef;

import hm.binkley.basilisk.flora.chef.store.ChefRecord;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public final class Chef {
    private final ChefRecord record;

    public <C> C as(final Chef.As<C> asChef) {
        return asChef.from(
                record.getId(), record.getCode(), record.getName());
    }

    public interface As<C> {
        C from(final Long id, final String code, final String name);
    }
}
