package hm.binkley.basilisk.x.kind.rest;

import lombok.Value;

import java.math.BigDecimal;

@Value
public final class KindRequest {
    String code;
    BigDecimal coolness;
}
