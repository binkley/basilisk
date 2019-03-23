package hm.binkley.basilisk.flora.source.rest;

import hm.binkley.basilisk.flora.location.rest.LocationResponse;
import hm.binkley.basilisk.flora.source.Sources;
import lombok.Builder;
import lombok.Value;

import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.stream.Collectors.toCollection;

@Builder
@Value
public final class SourceResponse {
    Long id;
    String code;
    String name;
    @Builder.Default
    Set<LocationResponse> availableAt = new LinkedHashSet<>();

    static Sources.As<SourceResponse, LocationResponse> using() {
        return (id, code, name, availableAt) -> new SourceResponse(
                id, code, name,
                availableAt.collect(toCollection(LinkedHashSet::new)));
    }
}
