package hm.binkley.basilisk.flora.chef.rest;

import lombok.Builder;
import lombok.Value;

@Builder
@Value
public final class ChefResponse {
    Long id;
    String code;
    String name;
}
