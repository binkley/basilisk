package hm.binkley.basilisk.flora.source.rest;

import hm.binkley.basilisk.Codeable;
import hm.binkley.basilisk.flora.location.rest.LocationController;
import hm.binkley.basilisk.flora.location.rest.LocationResponse;
import hm.binkley.basilisk.flora.source.Source;
import lombok.Builder;
import lombok.Value;

import java.util.SortedSet;
import java.util.TreeSet;

import static java.util.stream.Collectors.toCollection;

@Builder
@Value
public final class SourceResponse
        implements Codeable<SourceResponse> {
    Long id;
    String code;
    String name;
    @Builder.Default
    SortedSet<LocationResponse> availableAt = new TreeSet<>();

    public static SourceResponse of(final Source source) {
        return new SourceResponse(source.getId(), source.getCode(),
                source.getName(), source.getAvailableAt()
                .map(LocationController::toResponse)
                .collect(toCollection(TreeSet::new)));
    }
}
