package hm.binkley.basilisk.flora.rest;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Builder
@Data
@RequiredArgsConstructor
public final class ChefRequest {
    private final @Length(min = 3, max = 32) String name;

    public <C> C as(final ChefRequest.As<C> asChef) {
        return asChef.from(name);
    }

    public interface As<C> {
        C from(final String name);
    }
}
