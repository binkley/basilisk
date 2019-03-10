package hm.binkley.basilisk.flora.rest;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Builder
@Data
@RequiredArgsConstructor
public final class UnusedIngredientRequest {
    private final @Length(min = 3, max = 32) String name;

    public <T> T as(final UnusedIngredientRequest.As<T> asOther) {
        return asOther.from(getName());
    }

    public interface As<T> {
        T from(final String name);
    }
}
