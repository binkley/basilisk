package hm.binkley.basilisk.rest;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@Data
@RequiredArgsConstructor
public final class CockatriceRequest {
    private final @NotNull BigDecimal beakSize;

    public <T> T as(final CockatriceRequest.As<T> as) {
        return as.from(beakSize);
    }

    public interface As<T> {
        T from(final BigDecimal beakSize);
    }
}
