package hm.binkley.basilisk.flora.rest;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Builder
@Data
@RequiredArgsConstructor
public final class RecipeRequest {
    private final @Length(min = 3, max = 32) String name;

    public <T> T as(final RecipeRequest.As<T> asRecipe) {
        return asRecipe.from(name);
    }

    public interface As<T> {
        T from(final String name);
    }
}
