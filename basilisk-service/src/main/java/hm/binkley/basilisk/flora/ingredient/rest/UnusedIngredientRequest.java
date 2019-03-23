package hm.binkley.basilisk.flora.ingredient.rest;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Builder
@Data
@RequiredArgsConstructor
public final class UnusedIngredientRequest {
    private final @NotNull Long sourceId;
    private final @Length(min = 3, max = 8) String code;
    private final @Length(min = 3, max = 32) String name;
    private final @NotNull BigDecimal quantity;
    private final @NotNull Long chefId;

    public <I> I as(final UnusedIngredientRequest.As<I> asOther) {
        return asOther.from(
                getCode(), getSourceId(), getName(), getQuantity(),
                getChefId());
    }

    public interface As<I> {
        I from(final String code, final Long sourceId, final String name,
                final BigDecimal quantity, final Long chefId);
    }
}
