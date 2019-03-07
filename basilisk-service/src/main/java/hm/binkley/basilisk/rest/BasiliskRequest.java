package hm.binkley.basilisk.rest;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Builder
@Data
@RequiredArgsConstructor
public final class BasiliskRequest {
    private final @Length(min = 3, max = 32) String word;
    private final @NotNull Instant at;
    @Builder.Default
    private final @NonNull List<CockatriceRequest> cockatrices
            = new ArrayList<>();

    public <T, U> T as(final BasiliskRequest.As<T, U> asBasilisk,
            final CockatriceRequest.As<U> asCockatrice) {
        return asBasilisk.from(word, at, cockatrices.stream()
                .map(it -> asCockatrice.from(it.getBeakSize())));
    }

    public interface As<T, U> {
        T from(final String word, final Instant at,
                final Stream<U> cockatrices);
    }
}
