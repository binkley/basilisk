package hm.binkley.basilisk.flora.source.rest;

import hm.binkley.basilisk.flora.location.rest.LocationResponse;
import lombok.Builder;
import lombok.Value;

import java.util.LinkedHashSet;
import java.util.Set;

@Builder
@Value
public final class SourceResponse {
    Long id;
    String code;
    String name;
    @Builder.Default
    Set<LocationResponse> availableAt = new LinkedHashSet<>();
}
