package hm.binkley.basilisk.flora.location.rest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import static lombok.AccessLevel.PUBLIC;

@AllArgsConstructor(access = PUBLIC)
@Builder
@Value
public final class LocationResponse
        implements Comparable<LocationResponse> {
    Long id;
    String code;
    String name;

    @Override
    public int compareTo(final LocationResponse that) {
        return getCode().compareTo(that.getCode());
    }
}
