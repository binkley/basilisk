package hm.binkley.basilisk.flora.location.rest;

import hm.binkley.basilisk.Codeable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import static lombok.AccessLevel.PUBLIC;

@AllArgsConstructor(access = PUBLIC)
@Builder
@Value
public final class LocationResponse
        implements Codeable<LocationResponse> {
    Long id;
    String code;
    String name;
}
