package hm.binkley.basilisk.flora.location.rest;

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
}
