package hm.binkley.basilisk.flora.chef.rest;

import hm.binkley.basilisk.Codeable;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public final class ChefResponse
        implements Codeable<ChefResponse> {
    Long id;
    String code;
    String name;
}
