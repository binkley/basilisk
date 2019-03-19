package hm.binkley.basilisk.flora.rest;

import hm.binkley.basilisk.flora.domain.Source;
import lombok.Builder;
import lombok.Value;

@Builder
@Value
public final class SourceResponse {
    Long id;
    String name;

    static Source.As<SourceResponse> with() {
        return SourceResponse::new;
    }
}