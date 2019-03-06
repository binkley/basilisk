package hm.binkley.basilisk.rest;

import hm.binkley.basilisk.domain.Basilisk.As;
import hm.binkley.basilisk.service.BasiliskService;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

import static java.util.stream.Collectors.toCollection;

@Builder
@Value
public final class BasiliskResponse {
    Long id;
    String word;
    Instant at;
    String extra;
    @Builder.Default
    Set<CockatriceResponse> cockatrices = new LinkedHashSet<>();

    static As<BasiliskResponse> with(final BasiliskService service) {
        return (id, receivedAt, word, at, cockatrices) ->
                new BasiliskResponse(
                        id, word, at, service.extra(word), cockatrices
                        .map(it -> it.as(CockatriceResponse.with()))
                        .collect(toCollection(LinkedHashSet::new)));
    }
}
