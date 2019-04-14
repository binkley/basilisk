package hm.binkley.basilisk.x.kind.rest;

import lombok.Value;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Value
public final class KindRequest {
    @NotEmpty
    String code;
    @NotNull
    BigDecimal coolness;
}
