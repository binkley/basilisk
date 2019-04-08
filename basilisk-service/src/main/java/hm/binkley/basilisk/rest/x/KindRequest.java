package hm.binkley.basilisk.rest.x;

import lombok.Value;

import java.math.BigDecimal;

@Value
public final class KindRequest {
    String code;
    BigDecimal coolness;
}
