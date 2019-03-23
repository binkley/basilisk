package hm.binkley.basilisk.flora.chef.rest;

import hm.binkley.basilisk.flora.chef.Chef;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public final class ChefResponse {
    Long id;
    String code;
    String name;

    public static Chef.As<ChefResponse> using() {
        return ChefResponse::new;
    }
}
