package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.Chef;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public final class ChefResponse {
    Long id;
    String name;

    static Chef.As<ChefResponse> using() {
        return ChefResponse::new;
    }
}
