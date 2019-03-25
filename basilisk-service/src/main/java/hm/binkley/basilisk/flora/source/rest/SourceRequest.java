package hm.binkley.basilisk.flora.source.rest;

import hm.binkley.basilisk.flora.location.rest.LocationRequest;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Builder
@Data
@RequiredArgsConstructor
public final class SourceRequest {
    private final @Length(min = 3, max = 8) String code;
    private final @Length(min = 3, max = 32) String name;
    @Builder.Default
    private final @NotNull List<LocationRequest> availableAt
            = new ArrayList<>();
}
