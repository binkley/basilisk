package hm.binkley.basilisk.flora.domain;

import hm.binkley.basilisk.flora.domain.store.ChefRecord;
import lombok.EqualsAndHashCode;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.time.Instant;

@EqualsAndHashCode
@RequiredArgsConstructor
@ToString
public class Chef {
    private final ChefRecord record;

    public <T, U> T as(final Chef.As<T, U> asChef) {
        return asChef.from(
                record.getId(), record.getReceivedAt(), record.getCode(),
                record.getName());
    }

    public interface As<T, U> {
        T from(final Long id, final Instant receivedAt, final String code,
                final String name);
    }
}
