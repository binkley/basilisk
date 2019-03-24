package hm.binkley.basilisk.flora.location.rest;

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
}
