package hm.binkley.basilisk.rest;

import lombok.Builder;
import lombok.Data;

import java.time.OffsetDateTime;

@Builder
@Data
public final class BasiliskResponse {
    private final String word;
    private final OffsetDateTime when;
    private final String extra;
}
