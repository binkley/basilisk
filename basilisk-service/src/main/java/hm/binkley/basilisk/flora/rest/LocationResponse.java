package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.Locations;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public final class LocationResponse {
    Long id;
    String name;

    static Locations.As<LocationResponse> using() {
        return LocationResponse::new;
    }
}
