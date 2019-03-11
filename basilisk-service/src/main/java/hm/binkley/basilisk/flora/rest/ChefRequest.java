package hm.binkley.basilisk.flora.rest;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Builder
@Data
@RequiredArgsConstructor
public final class ChefRequest {
    private final @Length(min = 3, max = 3) String code;
    private final @Length(min = 3, max = 32) String name;

    public <T> T as(final ChefRequest.As<T> asChef) {
        return asChef.from(code, name);
    }

    public interface As<T> {
        T from(final String code, final String name);
    }
}
