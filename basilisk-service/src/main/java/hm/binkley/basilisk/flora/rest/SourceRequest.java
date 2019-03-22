package hm.binkley.basilisk.flora.rest;

import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Builder
@Data
@RequiredArgsConstructor
public final class SourceRequest {
    private final @Length(min = 3, max = 7) String code;
    private final @Length(min = 3, max = 32) String name;

    public <I> I as(final SourceRequest.As<I> asOther) {
        return asOther.from(getCode(), getName());
    }

    public interface As<I> {
        I from(final String code, final String name);
    }
}
