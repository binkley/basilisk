package hm.binkley.basilisk.rest;

import hm.binkley.basilisk.domain.Basilisk.As;
import hm.binkley.basilisk.service.BasiliskService;
import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Builder
@Value
public final class BasiliskResponse {
    Long id;
    String word;
    Instant at;
    String extra;

    static As<BasiliskResponse> with(final BasiliskService service) {
        return (id, receivedAt, word, at) -> new BasiliskResponse(
                id, word, at, service.extra(word));
    }
}
