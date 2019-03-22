package hm.binkley.basilisk.flora.rest;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Builder
@Data
@RequiredArgsConstructor
public final class LocationRequest {
    private final @Length(min = 3, max = 8) String code;
    private final @Length(min = 3, max = 32) String name;

    public <L> L as(final LocationRequest.As<L> asLocation) {
        return asLocation.from(code, name);
    }

    public interface As<L> {
        L from(final String code, final String name);
    }
}
