package hm.binkley.basilisk.flora.rest;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;

@Builder
@Data
@RequiredArgsConstructor
public final class UsedIngredientRequest {
    private final @Length(min = 3, max = 32) String name;
    private final @NotNull Long chefId;

    public <I> I as(final UsedIngredientRequest.As<I> asOther) {
        return asOther.from(getName(), getChefId());
    }

    public interface As<I> {
        I from(final String name, final Long chefId);
    }
}
