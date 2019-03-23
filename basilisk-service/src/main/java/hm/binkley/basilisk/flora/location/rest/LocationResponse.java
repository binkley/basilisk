package hm.binkley.basilisk.flora.location.rest;

import hm.binkley.basilisk.flora.location.Locations;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import static lombok.AccessLevel.PUBLIC;

@AllArgsConstructor(access = PUBLIC)
@Builder
@Value
public final class LocationResponse {
    Long id;
    String code;
    String name;

    public static Locations.As<LocationResponse> using() {
        return LocationResponse::new;
    }
}
