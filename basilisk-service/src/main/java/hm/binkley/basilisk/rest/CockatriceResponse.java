package hm.binkley.basilisk.rest;

import hm.binkley.basilisk.domain.Cockatrice.As;
import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Builder
@Value
public final class CockatriceResponse {
    Long id;
    BigDecimal beakSize;

    static As<CockatriceResponse> using() {
        return (id, receivedAt, beakSize) ->
                new CockatriceResponse(id, beakSize);
    }
}

