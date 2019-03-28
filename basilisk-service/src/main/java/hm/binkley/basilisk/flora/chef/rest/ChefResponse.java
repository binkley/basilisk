package hm.binkley.basilisk.flora.chef.rest;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public final class ChefResponse
        implements Comparable<ChefResponse> {
    Long id;
    String code;
    String name;

    @Override
    public int compareTo(final ChefResponse that) {
        return getCode().compareTo(that.getCode());
    }
}
