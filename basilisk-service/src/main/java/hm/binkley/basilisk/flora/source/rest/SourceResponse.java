package hm.binkley.basilisk.flora.source.rest;

import hm.binkley.basilisk.Codeable;
import hm.binkley.basilisk.flora.location.rest.LocationResponse;
import lombok.Builder;
import lombok.Value;

import java.util.SortedSet;
import java.util.TreeSet;

@Builder
@Value
public final class SourceResponse
        implements Codeable<SourceResponse> {
    Long id;
    String code;
    String name;
    @Builder.Default
    SortedSet<LocationResponse> availableAt = new TreeSet<>();
}
